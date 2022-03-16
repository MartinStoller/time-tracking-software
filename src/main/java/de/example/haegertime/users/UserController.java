package de.example.haegertime.users;

import de.example.haegertime.authorization.MyUserDetails;
import de.example.haegertime.projects.Project;
import de.example.haegertime.timetables.TimeTableDay;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    /**
     if no Requestparam is given, results are not sorted. If sortParam == "role", sort by role. If sortParam == "abc",
     sort by last name alphabetically
     */
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<User> getAllUsers(@RequestParam(required = false) String sortBy) {
        return userService.getAllUsers(sortBy);
    }


//    public ResponseEntity<String> createUser(@RequestBody User user) {
//        try {
//            this.userService.createUser(user);
//            return new ResponseEntity<>("User gespeichert", HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Eingabedaten falsch: Error occured " + e.getMessage(),
//                    HttpStatus.BAD_GATEWAY);
//        }

//    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public ResponseEntity<User> getById(@PathVariable long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    //Ausgabe User anhand eines Keywords
    @GetMapping("/search/{keyword}")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<User> getByKeyword(@PathVariable("keyword") String keyword) {
        return this.userService.findByLastByFirstByEmail(keyword);
    }

    @GetMapping("/myProjects")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public Set<Project> getMyProjects(Authentication authentication) {
        return userService.getMyProjects(authentication.getName());
    }


    @GetMapping("/personalOvertime")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<Double> getPersonalOvertimeBalance(Authentication authentication) {
        //returns a List of 3 Values[expected hours sum, actual hours sum, resulting Overtimebalance
        String email = authentication.getName();
        return userService.getOvertimeBalance(email);

    }


    @GetMapping("/OvertimeByEmail/{email}")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<?> getOvertimeBalanceByEmail(@PathVariable String email) {
        //returns a List of 3 Values[expected hours sum, actual hours sum, resulting Overtimebalance
        return userService.getOvertimeBalance(email);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public ResponseEntity<User> currentUser( ) {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication1.getName();
        return ResponseEntity.ok(userService.getByUsername(email));
    }

    @GetMapping("/user-by-email/{email}")
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.getByUsername(email);
    }

    @GetMapping("/showOwnWorkdays")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<TimeTableDay> showOwnWorkdays(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end,
            Authentication authentication) {
        /**
         * Übersicht über die Arbeitstage eines Nutzers
         * @param start: start Datum ab welchem man die Arbeitstage sehen will (defaults to 01.01.1900)
         * @param end: end Datum bis zu welchem man die Arbeitstage sehen will (defaults to 01.01.2099)
         * @return List of TimetableDays in given Daterange of Logged in Employee
         */
        return userService.showOwnWorkdays(authentication.getName(), start, end);
    }

    /**
     * Account-Daten ändern, Employee darf seine E-Mail-Adresse und Rolle nicht ändern.
     *
     * @param user       die neuen Account-Daten
     * @param loggedUser der eingeloggte Benutzer
     * @return geupdatete UserDetails
     */
    @PutMapping("/current-user/update")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public ResponseEntity<User> updateUserDetails(@RequestBody User user, @AuthenticationPrincipal MyUserDetails loggedUser) {
        String username = loggedUser.getUsername();
        User logged = userService.getByUsername(username);
        return ResponseEntity.ok(userService.updateUserDetails(user, logged));
    }

    /**
     * Username eines Nutzers ändern (ADMIN)
     *
     * @param id          Nutzer ID
     * @param newUserName neue E-Mail-Adresse
     * @return aktualisierter Nutzer
     */
    @PutMapping("/update/username/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> changeUserName(Long id, @RequestParam("email") String newUserName) {
        return ResponseEntity.ok(userService.updateUserName(id, newUserName));
    }


    @PutMapping("/deactiv/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Reaktivieren einen Benutzer
     *
     * @param id Benutzer ID
     * @return
     */
    @PutMapping("/reactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reactivUser(@PathVariable Long id) {
        userService.reactivateUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateRoleUser(@PathVariable Long id, @RequestParam("role") String role) {
        return ResponseEntity.ok(userService.updateRole(id, role));
    }


    @PostMapping("/register/timetable")

    public void registerNewTimeTable(@RequestBody TimeTableDay timeTableDay, Authentication authentication) {
        String username = authentication.getName();
        userService.registerNewTimeTable(timeTableDay, username);
    }

    @GetMapping("/holidays/rest")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public double showMyRestHolidays(Authentication authentication) {
        String username = authentication.getName();
        return userService.showMyRestHolidays(username);
    }

    /**
     * Sendet der Bookkeeper eine Anfrage zum Urlaubsbeantragen,
     * der Bookkeeper kann entweder akzeptieren oder ablehnen
     *
     * @param employeeId    Nutzer ID
     * @param dayId //TODO Cedrik: vervollständigen
     * @param duration //TODO Cedrik: vervollständigen
     */
    @PostMapping("/apply/holiday/{id}")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public ResponseEntity<Void> applyForHoliday(@PathVariable("id") Long employeeId, @RequestParam Long dayId,
                                                @RequestParam double duration) {
        userService.applyForHoliday(employeeId, dayId, duration);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/holiday/decline/employee/{id}")
    @PreAuthorize("hasRole('BOOKKEEPER')")
    public ResponseEntity<Void> declineRequestedHoliday(@PathVariable("id") Long employeeId) {
        userService.declineForHoliday(employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/holidays")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<TimeTableDay> showAllMyHolidays(Authentication authentication) {
        String email = authentication.getName();
        return userService.showAllMyHolidays(email);
    }





    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(@RequestBody User toUpdateUser, @PathVariable Long id) {
        return userService.updateUser(toUpdateUser, id);
    }


}




