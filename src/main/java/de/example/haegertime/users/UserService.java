package de.example.haegertime.users;

import de.example.haegertime.advice.EmailAlreadyExistsException;
import de.example.haegertime.advice.InvalidRoleException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.email.EmailService;
import de.example.haegertime.timetables.TimeTableDay;
import de.example.haegertime.timetables.TimeTableRepository;
import lombok.AllArgsConstructor;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@AllArgsConstructor //takes care of constructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TimeTableRepository timeTableRepository;

    public List<User> getAllUsers(String sortBy) {
        if(sortBy == null){
            return userRepository.findAll();
        }
        else if(sortBy.equals("abc")){
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, ("last")));}
        else if(sortBy.equals("role")){
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, ("role")));}
        else{
            throw new InvalidParameterException("Requestparameter must either be abc, role or null.");
        }
    }


    public void createUser(User user) {
        String mail = user.getEmail();
        if (userRepository.existsByEmail(mail)){
            throw new EmailAlreadyExistsException("Diese Email wird bereits verwendet.");
        }
        userRepository.save(user);
        emailService.send(user.getEmail(), "Dein Haegertime Account wurde erstellt",
                emailService.getEmailText(user.getFirst(), "Neuerstellung deines Accounts"));
    }


    public User findByIdUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.getById(id);
        } else {
            throw new ItemNotFoundException("Dieser User ist nicht in der Datenbank");
        }
    }

    public List<User> findByKeywordUser(String keyword) {
        return userRepository.findByKeyword(keyword);
    }


    //todo change to only Admin
    public void deleteUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            userRepository.deleteById(id);
            emailService.send(user.getEmail(), "Dein Haegertime Account wurde gelöscht",
                    emailService.getEmailText(user.getFirst(), "Löschung derines Accounts"));
        } else {
            throw new ItemNotFoundException("Dieser User ist nicht in der Datenbank");
        }
    }

    public User getUserByUserName(String username) {
        return userRepository.getUserByUserEmail(username);
    }


    public User updateUserDetails(User user, User loggedUser) {
        User updateUser = userRepository.getUserByUserEmail(loggedUser.getEmail());
        updateUser.setPassword(user.getPassword());
        updateUser.setFirst(user.getFirst());
        updateUser.setLast(user.getLast());
        updateUser.setEmail(loggedUser.getEmail()); //email darf nicht selbst ändern
        updateUser.setRole(loggedUser.getRole());   //role darf nicht selbst ändern
        updateUser.setId(loggedUser.getId());
        updateUser.setFrozen(loggedUser.isFrozen());
        userRepository.save(updateUser);
        return updateUser;
    }


    public User updateUserName(Long id, String newUserName) {
        User updateUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mit Id "+id+"" +
                        " ist nicht in der DB")
        );
        String email = updateUser.getEmail();
        String first = updateUser.getFirst();
        updateUser.setEmail(newUserName);
        userRepository.save(updateUser);
        emailService.send(email, "Dein Profil wurde geupdated",
                emailService.getEmailText(first, "Update deiner Accountdetails"));
        return updateUser;
    }


    public void deactivUser(Long id) {
        User deactivUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mid Id "+id+
                        " ist nicht in der DB")
        );
        deactivUser.setFrozen(false);
        userRepository.save(deactivUser);
    }


    public void reactivUser(Long id) {
        User reactivUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mid Id "+id+
                        " ist nicht in der DB")
        );
        reactivUser.setFrozen(true);
        userRepository.save(reactivUser);
    }

    public User updateRoleUser(Long id, String role) {
        User updateRoleUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mid Id "+id+" ist nicht in der DB")
        );
        if(role.equals("ADMIN") || role.equals("EMPLOYEE") || role.equals("BOOKKEEPER") ) {
            updateRoleUser.setRole(Role.valueOf(role));
            userRepository.save(updateRoleUser);
            return updateRoleUser;
        } else {
            throw new InvalidRoleException("Die eingegebene Role ist ungültig");
        }
    }


}








