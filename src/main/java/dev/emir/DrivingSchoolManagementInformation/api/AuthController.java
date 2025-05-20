package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.LoginRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.LoginResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.RegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.RegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.security.JwtTokenProvider;
import dev.emir.DrivingSchoolManagementInformation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login attempt for user: " + loginRequest.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            User user = (User) authentication.getPrincipal();
            
            System.out.println("Login successful for user: " + user.getUsername() + " with role: " + user.getRole());
            
            Long linkedEntityId = null;
            if (user.getStudent() != null) {
                linkedEntityId = user.getStudent().getId();
            } else if (user.getInstructor() != null) {
                linkedEntityId = user.getInstructor().getId();
            } else if (user.getEmployee() != null) {
                linkedEntityId = user.getEmployee().getId();
            }
            
            return ResponseEntity.ok(new LoginResponse(
                jwt,
                user.getUsername(),
                user.getRole().name(),
                user.getId(),
                linkedEntityId
            ));
        } catch (BadCredentialsException e) {
            System.out.println("Login failed for user: " + loginRequest.getUsername() + " - Invalid credentials");
            throw e;
        } catch (Exception e) {
            System.out.println("Login failed for user: " + loginRequest.getUsername() + " - Error: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = userService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}
