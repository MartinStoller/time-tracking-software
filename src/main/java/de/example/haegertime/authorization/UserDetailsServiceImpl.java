package de.example.haegertime.authorization;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Login daten = useremail + password
        User user = userRepository.getUserByEmail(username).orElseThrow(()->new ItemNotFoundException(""));
        if(user == null) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden");
        }
        return new MyUserDetails(user);
    }
}
