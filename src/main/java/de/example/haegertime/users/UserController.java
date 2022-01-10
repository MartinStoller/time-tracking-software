package de.example.haegertime.users;

import de.example.haegertime.authorization.MyUserDetails;
import de.example.haegertime.timetables.TimeTableDay;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/user/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){return userService.getAllUsers();}

    //todo only Admin
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            this.userService.createUser(user);
            return ResponseEntity.ok("User gespeichert");
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


    //todo only Admin
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

    @PutMapping("/registertimetable")
    public String registerNewTimeTable(@RequestBody TimeTableDay timeTableDay, Principal principal) {
        String username = principal.getName();
        return userService.registerNewTimeTable(timeTableDay, username);
    }
}




