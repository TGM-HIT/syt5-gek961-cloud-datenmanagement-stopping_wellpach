package org.example.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.user.authentication.config.JwtService;
import org.example.user.entities.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User getUserFromRequest(HttpServletRequest request) {

        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;

        final String jwt = authHeader.substring("Bearer ".length());
        final String userid = jwtService.extractUserId(jwt);
        if(userid == null) return null;

        User user = userRepository.findByUserid(userid).orElse(null);

        return user;
    }

    public void clearDB() {
        userRepository.deleteAll();
    }
}