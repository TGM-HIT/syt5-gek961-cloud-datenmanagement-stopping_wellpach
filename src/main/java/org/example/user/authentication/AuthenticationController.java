package org.example.user.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.user.UserRepository;
import org.example.user.UserService;
import org.example.user.authentication.config.AuthenticationService;
import org.example.user.authentication.protocols.request.AuthenticationRequest;
import org.example.user.authentication.protocols.request.RegisterRequest;
import org.example.user.entities.Role;
import org.example.user.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") //TODO: cross origins
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> changeEmail(HttpServletRequest request, @RequestBody RegisterRequest registerRequest) {

        User user = userService.getUserFromRequest(request);
        if (user == null) return null;

        if(user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Forbidden! Not Admin!");
        }

        return service.register(registerRequest);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return service.authenticate(request);
    }
}
