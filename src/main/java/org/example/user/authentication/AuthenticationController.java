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
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") //TODO: cross origins
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    public AuthenticationController(AuthenticationService service, UserService userService) {
        this.service = service;
        this.userService = userService;

        this.userService.clearDB();

        try (FileReader reader = new FileReader("file.json")) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            System.out.println(jsonObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
