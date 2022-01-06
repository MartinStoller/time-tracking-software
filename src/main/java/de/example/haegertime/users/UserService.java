package de.example.haegertime.users;

import de.example.haegertime.advice.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

}








