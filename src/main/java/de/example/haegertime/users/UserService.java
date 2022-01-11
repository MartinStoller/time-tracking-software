package de.example.haegertime.users;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor //takes care of constructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void createUser(User user) {
        Optional<User> userById = userRepository.findById(user.getId());
        if (userById.isPresent()) {
            throw new IllegalArgumentException();
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
        userRepository.save(updateUser);
        emailService.send(loggedUser.getEmail(), "Dein Haegertime Account wurde erstellt",
                emailService.getEmailText(user.getFirst(), "Neuerstellung deines Accounts"));
        return updateUser;
    }
}








