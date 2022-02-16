package de.example.haegertime.authorization;

import org.apache.maven.artifact.repository.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/login")
public class AuthController {
    @GetMapping
    public AuthenticationBean basicauth() {
        return new AuthenticationBean("You are authenticated");
    }
}
