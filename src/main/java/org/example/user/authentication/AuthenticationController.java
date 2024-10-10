package org.example.user.authentication;

import lombok.AllArgsConstructor;
import org.example.user.UserRepository;
import org.example.user.UserService;
import org.example.user.authentication.config.AuthenticationService;
import org.example.user.authentication.protocols.request.AuthenticationRequest;
import org.example.user.authentication.protocols.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") //TODO: cross origins
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return service.register(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return service.authenticate(request);
    }
}
