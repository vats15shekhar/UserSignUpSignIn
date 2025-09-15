package com.personal.UserSignupSignin.Controller;

import com.personal.UserSignupSignin.Configuration.SecurityConfig;
import com.personal.UserSignupSignin.DTO.LoginRequest;
import com.personal.UserSignupSignin.DTO.SignUpRequest;
import com.personal.UserSignupSignin.Entity.User;
import com.personal.UserSignupSignin.Service.JwtService;
import com.personal.UserSignupSignin.Service.UserService;
import com.personal.UserSignupSignin.UserRoles.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final SecurityConfig securityConfig;

    public AuthController(UserService userService, JwtService jwtService, SecurityConfig securityConfig) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.securityConfig = securityConfig;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username taken");
        }
        userService.save(new User(request.getUsername(), request.getPassword(), Role.USER));
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null || !securityConfig.passwordEncoder().matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(jwt);
    }
}

