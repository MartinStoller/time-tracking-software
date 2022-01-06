package de.example.haegertime.users;

import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

    public List<User> getAllUsers(){return  userRepository.findAll();}

    public User getUser(Long id) throws InstanceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("User with id " + id + " not found"));
        return user;
    }
}


