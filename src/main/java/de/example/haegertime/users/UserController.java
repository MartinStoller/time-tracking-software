package de.example.haegertime.users;

import de.example.haegertime.authorization.MyUserDetails;
import de.example.haegertime.projects.Project;
import de.example.haegertime.timetables.TimeTableDay;
import de.example.haegertime.timetables.TimeTableService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping("api/user/")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final TimeTableService ttService;

    @GetMapping("/all")
    /**
       if no Requestparam is given, results are not sorted. If sortParam == "role", sort by role. If sortParam == "abc",
       sort by last name alphabetically
     */
    public List<User> getAllUsers(@RequestParam(required = false) String sortBy){
        return userService.getAllUsers(sortBy);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            this.userService.createUser(user);
            return new ResponseEntity<>("User gespeichert", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Eingabedaten falsch: Error occured " + e.getMessage(),
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findByIdUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.findByIdUser(id));
    }

    //Ausgabe User anhand eines Keywords
    @GetMapping("/search/{keyword}")
    public List<User> getByKeyword(@PathVariable("keyword") String keyword) {
        return this.userService.findByKeywordUser(keyword);
    }

    @GetMapping("/myProjects")
    public LinkedHashSet<Project> getMyProjects(Principal principal){
        return userService.getMyProjects(principal.getName());
    }

    @GetMapping("/personalOvertime")
    public List<Double> getPersonalOvertimeBalance(Principal principal){
        //returns a List of 3 Values[expected hours sum, actual hours sum, resulting Overtimebalance
        String email = principal.getName();
        return userService.getOvertimeBalance(email);

    }

    @GetMapping("/OvertimeByEmail/{email}")
    public List<?> getOvertimeBalanceById(@PathVariable String email){
        //returns a List of 3 Values[expected hours sum, actual hours sum, resulting Overtimebalance
        return userService.getOvertimeBalance(email);
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Anzeigen die Informationen über den aktuellen Benutzer
     * @param principal
     * @return
     */
    @GetMapping("/myacc")
    public ResponseEntity<User> currentUser(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(userService.getUserByUserName(username));
    }

    @GetMapping("/showOwnWorkdays")
    public List<TimeTableDay> ShowOwnWorkdays(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end,
            Principal principal) {
        /**
         * Übersicht über die Arbeitstage eines Nutzers
         * @param start: start Datum ab welchem man die Arbeitstage sehen will (defaults to 01.01.1900)
         * @param end: end Datum bis zu welchem man die Arbeitstage sehen will (defaults to 01.01.2099)
         * @return List of TimetableDays in given Daterange of Logged in Employee
         */
        return userService.showOwnWorkdays(principal.getName(), start, end);
    }

    /**
     * Account-Daten ändern, Employee darf seine E-Mail-Adresse und Rolle nicht ändern.
     * @param user die neuen Account-Daten
     * @param loggedUser  der eingeloggte Benutzer
     * @return
     */
    @PutMapping("/myacc/update")
    public ResponseEntity<User> updateUserDetails(@RequestBody User user, @AuthenticationPrincipal MyUserDetails loggedUser) {
        String username = loggedUser.getUsername();
        User logged = userService.getUserByUserName(username);
        return ResponseEntity.ok(userService.updateUserDetails(user, logged));
    }


    /**
     * Username eines Nutzers ändern (ADMIN)
     * @param id    Nutzer ID
     * @param newUserName neue E-Mail-Adresse
     * @return  aktualisierter Nutzer
     */
    @PutMapping("/update/username/{id}")
    public ResponseEntity<User> updateUserName(Long id,@RequestParam("email") String newUserName) {
        return ResponseEntity.ok(userService.updateUserName(id, newUserName));
    }

    /**
     * Deaktivieren einen Benutzer
     * @param id Benutzer ID
     * @return
     */
    @PutMapping("/deactiv/{id}")
    public ResponseEntity<Void> deactivUser(@PathVariable Long id) {
        userService.deactivUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Reaktivieren einen Benutzer
     * @param id Benutzer ID
     * @return
     */
    @PutMapping("/reactiv/{id}")
    public ResponseEntity<Void> reactivUser(@PathVariable Long id) {
        userService.reactivUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updaterole/{id}")
    public ResponseEntity<User> updateRoleUser(@PathVariable Long id,@RequestParam("role") String role) {
        return ResponseEntity.ok(userService.updateRoleUser(id, role));
    }

}




