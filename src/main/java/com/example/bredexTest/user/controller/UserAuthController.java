package com.example.bredexTest.user.controller;

import com.example.bredexTest.jwt.JwtTokenValidator;
import com.example.bredexTest.jwt.TokenBlacklist;
import com.example.bredexTest.user.service.UserService;
import com.example.bredexTest.user.dto.request.UserLoginRequestDTO;
import com.example.bredexTest.user.dto.request.UserRegisterRequestDTO;
import com.example.bredexTest.user.dto.response.UserLoginResponseDTO;
import com.example.bredexTest.user.exception.EmailAlreadyExistException;
import com.example.bredexTest.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserAuthController {

    @Autowired
    private UserService authService;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequestDTO signupRequest) {
        try{
            User user = authService.registerUser(signupRequest);
            return ResponseEntity.ok(user);
        }catch (EmailAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use");
        }

    }

    @PostMapping("/auth/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginRequestDTO loginRequest) {
        return authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String token) {

        if (!jwtTokenValidator.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        tokenBlacklist.blacklist(token);
        authService.logout();

        return ResponseEntity.ok("Logged out successfully");
    }
}