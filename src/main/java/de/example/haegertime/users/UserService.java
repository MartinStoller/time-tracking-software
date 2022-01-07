package de.example.haegertime.users;

import de.example.haegertime.advice.InvalidRoleException;
import de.example.haegertime.advice.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import de.example.haegertime.users.User;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void createUser(User user) {
        Optional<User> userById = userRepository.findById(user.getId());
        if (userById.isPresent()) {
            throw new IllegalArgumentException();
        }
        userRepository.save(user);
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
            userRepository.deleteById(id);
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
        updateUser.setEmail(newUserName);
        userRepository.save(updateUser);
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
        reactivUser.setFrozen(false);
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








