package org.example.user.authentication.config;

import lombok.RequiredArgsConstructor;
import org.example.JsonHelper;
import org.example.user.UserRepository;
import org.example.user.authentication.protocols.request.AuthenticationRequest;
import org.example.user.authentication.protocols.request.RegisterRequest;
import org.example.user.entities.Role;
import org.example.user.entities.User;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register(RegisterRequest request) throws IOException {

        if(this.userRepository.findByUserid(request.getUserid()).isPresent()) {
            System.out.println("User with userid: " + request.getUserid() + " already exists!");

            return ResponseEntity.status(409).body("User with email: " + request.getUserid() + " already exists!");
        }

        var user = User.builder()
                .name(request.getName())
                .userid(request.getUserid())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwtToken);

        return ResponseEntity.ok().body(JSONObject.toJSONString(map));
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {

        User user = userRepository.findByUserid(request.getUserid()).orElseThrow(() -> new UsernameNotFoundException("No User with Userid: " + request.getUserid()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserid(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwtToken);

        return ResponseEntity.status(200)
                .body(JsonHelper.toJSON(map));
    }
}
