package de.example.haegertime.authorization;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/login")
public class AuthController {
    @GetMapping
    public AuthenticationBean basicauth() {
        return new AuthenticationBean("You are authenticated");
    }
}
