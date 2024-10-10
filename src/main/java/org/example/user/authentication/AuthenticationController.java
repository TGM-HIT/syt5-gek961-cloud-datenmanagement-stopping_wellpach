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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") //TODO: cross origins
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationService service, UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        this.userService.clearDB();

        try (FileReader reader = new FileReader("file.json")) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray jsonArray = new JSONArray(tokener);
            System.out.println(jsonArray.toString(4));

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Role role = Role.READER;
                switch(jsonObject.getString("role")) {
                    case "admin":
                        role = Role.ADMIN;
                        break;

                    case "moderator":
                        role = Role.MODERATOR;
                        break;

                    case "reader":
                        role = Role.READER;
                        break;
                }

                var user = User.builder()
                        .name(jsonObject.getString("name"))
                        .userid(jsonObject.getString("userid"))
                        .password(passwordEncoder.encode(jsonObject.getString("password")))
                        .role(role)
                        .build();

                userRepository.save(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> changeEmail(HttpServletRequest request, @RequestBody RegisterRequest registerRequest) {

        User user = userService.getUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.status(403).body("UNAUTHORIZED");
        }

        if(user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("UNAUTHORIZED");
        }

        return service.register(registerRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return service.authenticate(request);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> authenticate(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.status(403).body("UNAUTHORIZED");
        }

        return ResponseEntity.status(200).body(user.getRole().toString());
    }
}
