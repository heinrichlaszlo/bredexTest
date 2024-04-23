package com.example.bredexTest.user.service;

import com.example.bredexTest.jwt.JwtTokenGenerator;
import com.example.bredexTest.user.dto.request.UserRegisterRequestDTO;
import com.example.bredexTest.user.dto.response.UserLoginResponseDTO;
import com.example.bredexTest.user.exception.EmailAlreadyExistException;
import com.example.bredexTest.user.model.User;
import com.example.bredexTest.user.repository.UserRepository;
import com.example.bredexTest.userLogout.model.UserLogout;
import com.example.bredexTest.userLogout.repository.UserLogoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenGenerator jwtTokenUtil;

    private AuthenticationManager authenticationManager;

    @Autowired
    private UserLogoutRepository userLogoutRepository;

    /**
     * This method is used to register a new user.
     * It first checks if the provided email already exists in the database. If it does, it throws an EmailAlreadyExistException.
     * Then, it creates a new User object using the provided email, username, and password. The password is encoded using the PasswordEncoder.
     * Finally, it saves the new User to the database using the UserRepository and returns the saved User.
     *
     * @param userRegisterRequestDTO This is the DTO object that contains the email, username, and password of the new User.
     * @return User This returns the saved User object.
     * @throws EmailAlreadyExistException if the provided email already exists in the database.
     */
    @Transactional
    public User registerUser(UserRegisterRequestDTO userRegisterRequestDTO) {
        if (userRepository.existsByEmail(userRegisterRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("Email is already in use");
        }

        return userRepository.save(User.builder().email(userRegisterRequestDTO.getEmail()).username(userRegisterRequestDTO.getUsername()).password(passwordEncoder.encode(userRegisterRequestDTO.getPassword())).build());

    }
    /**
     * This method is used to authenticate a user.
     * It first retrieves the User from the database using the provided email. If the User does not exist, it throws a RuntimeException.
     * Then, it checks if the provided password matches the User's password. If not, it throws a RuntimeException.
     * It generates an access token and a refresh token for the User using the JwtTokenGenerator.
     * Finally, it returns a UserLoginResponseDTO object that contains the generated tokens.
     *
     * @param email This is the email of the User to be authenticated.
     * @param password This is the password of the User to be authenticated.
     * @return UserLoginResponseDTO This returns a DTO object that contains the generated access and refresh tokens.
     * @throws RuntimeException if the User does not exist or the provided password does not match the User's password.
     */
    public UserLoginResponseDTO authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).get();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect username or password");
        }

        String accessToken = jwtTokenUtil.generateAccessToken(email);
        String refreshToken = jwtTokenUtil.generateRefreshToken(email);

        return UserLoginResponseDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    /**
     * This method is used to log out a user.
     * It first retrieves the current authenticated user's email from the SecurityContext.
     * Then, it finds the User in the database using the retrieved email. If the User does not exist, it throws an IllegalArgumentException.
     * It creates a new UserLogout object using the current User and the current time, and saves it to the database using the UserLogoutRepository.
     *
     * @throws IllegalArgumentException if the User does not exist.
     */
    @Transactional
    public void logout() {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));



        userLogoutRepository.save(UserLogout.builder().user(currentUser).logoutTime(LocalDateTime.now()).build());
    }
}

