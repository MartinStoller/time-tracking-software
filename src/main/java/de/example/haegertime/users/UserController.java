package de.example.haegertime.users;

import de.example.haegertime.authorization.MyUserDetails;
import de.example.haegertime.projects.Project;
import de.example.haegertime.timetables.TimeTableDay;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            this.userService.createUser(user);
            return new ResponseEntity<>("User gespeichert", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Eingabedaten falsch: Error occured " + e.getMessage(),
                    HttpStatus.BAD_GATEWAY);
        }
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
    //TODO Cedrik: Principal ist ein zu komplizierter Input. Hier brauchst du eigentlich garkeinen input, weil Spring den eingeloggten User kennt.
    // https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
    @GetMapping("/myProjects")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    //TODO Cedrik: als return Wert reicht hier Set<Project>.
    public LinkedHashSet<Project> getMyProjects(Principal principal) {
        return userService.getMyProjects(principal.getName());
    }

    //TODO Cedrik: Principal ist ein zu komplizierter Input. Hier brauchst du eigentlich garkeinen input, weil Spring den eingeloggten User kennt.
    // https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
    @GetMapping("/personalOvertime")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<Double> getPersonalOvertimeBalance(Principal principal) {
        //returns a List of 3 Values[expected hours sum, actual hours sum, resulting Overtimebalance
        String email = principal.getName();
        return userService.getOvertimeBalance(email);

    }

    //TODO Cedrik: name falsch.
    @GetMapping("/OvertimeByEmail/{email}")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public List<?> getOvertimeBalanceById(@PathVariable String email) {
        //returns a List of 3 Values[expected hours sum, actual hours sum, resulting Overtimebalance
        return userService.getOvertimeBalance(email);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Anzeigen die Informationen über den aktuellen Benutzer
     *
     * @param principal
     * @return
     */
    //TODO Cedrik: Principal ist ein zu komplizierter Input. Hier brauchst du eigentlich garkeinen input, weil Spring den eingeloggten User kennt.
    // https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    public ResponseEntity<User> currentUser(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @GetMapping("/showOwnWorkdays")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    //TODO Cedrik: Auch hier Principal aus dem context holen.
    public List<TimeTableDay> showOwnWorkdays(
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
     *
     * @param user       die neuen Account-Daten
     * @param loggedUser der eingeloggte Benutzer
     * @return //TODO Cedrik: @return vervollständigen
     */
    @PutMapping("/current-user/update")
    @PreAuthorize("hasRole('ADMIN')")
    //TODO Cedrik: Warum ist die Implementierung mit der Authentication hier anders? - Hier auch aus dem Context holen.
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
    //TODO Cedrik: intuitiv wäre auch eine Methode changeMyUsername (oder so)
    public ResponseEntity<User> updateUserName(Long id, @RequestParam("email") String newUserName) {
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
    //TODO Cedrik: Principal aus dem context holen.
    public String registerNewTimeTable(@RequestBody TimeTableDay timeTableDay, Principal principal) {
        String username = principal.getName();
        return userService.registerNewTimeTable(timeTableDay, username);
    }

    @GetMapping("/holidays/rest")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    //TODO Cedrik: Principal aus dem context holen.
    public double showMyRestHolidays(Principal principal) {
        String username = principal.getName();
        return userService.showMyRestHolidays(username);
    }


    /**
     * Sendet der Bookkeeper eine Anfrage zum Urlaubsbeantragen,
     * der Bookkeeper kann entweder akzeptieren oder ablehnen
     *
     * @param employeeId //TODO Cedrik: vervollständigen
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
    //TODO Cedrik: Name eher declineRequestedHoliday. - Werden immer alle Urlaubsanträge gleichzeitig abgeleht?
    public ResponseEntity<Void> declineForHoliday(@PathVariable("id") Long employeeId) {
        userService.declineForHoliday(employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/holidays")
    @PreAuthorize("hasRole('ADMIN')or hasRole('EMPLOYEE') or hasRole('BOOKKEEPER')")
    //TODO Cedrik: Principal aus dem context holen.
    public List<TimeTableDay> showAllMyHolidays(Principal principal) {
        String email = principal.getName();
        return userService.showAllMyHolidays(email);
    }
}




