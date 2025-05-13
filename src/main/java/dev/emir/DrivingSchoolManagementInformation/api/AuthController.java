package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.dto.response.AuthResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.request.LoginRequest;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtUtil.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))));

        Long linkedEntityId = null;
        switch (user.getRole()) {
            case STUDENT -> {
                if (user.getStudent() != null)
                    linkedEntityId = user.getStudent().getId();
            }
            case INSTRUCTOR -> {
                if (user.getInstructor() != null)
                    linkedEntityId = user.getInstructor().getId();
            }
            case EMPLOYEE -> {
                if (user.getEmployee() != null)
                    linkedEntityId = user.getEmployee().getId();
            }
            default -> {
            }
        }

        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name(),
                user.getId(),
                linkedEntityId
        ));
    }
}
