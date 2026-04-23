package com.project.ecommerce.service;

import com.project.ecommerce.dto.*;
import com.project.ecommerce.entity.PasswordResetToken;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.enums.Role;
import com.project.ecommerce.repository.PasswordResetTokenRepository;
import com.project.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;

    public AuthResponseDto register(RegisterRequestDto  registerRequestDto) {
        User user = new User();
        user.setDisplayName(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponseDto(token, "User created");


    }

    public AuthResponseDto registerAdmin(RegisterRequestDto registerRequestDto) {
        User admin = new User();
        admin.setDisplayName(registerRequestDto.getUsername());
        admin.setEmail(registerRequestDto.getEmail());
        admin.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        String token = jwtService.generateToken(admin);

        return new AuthResponseDto(token, "Admin created");
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {



            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );



        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);


        return new AuthResponseDto(token, "User logged in");

    }

    public void forgotPassword(ForgotPasswordRequestDto request){

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        //Do nothing if user not found, because security

        if(userOptional.isEmpty()){
            return;
        }

        User user = userOptional.get();

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        passwordResetToken.setUser(user);

        passwordResetTokenRepository.save(passwordResetToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Password Reset");
        message.setText("Your token to reset your password is: " + passwordResetToken.getToken() + " it will expire in 15 Minutes");
        mailSender.send(message);



    }

    public String resetPassword(ResetPasswordRequestDto requestDto){

        PasswordResetToken token = passwordResetTokenRepository.findByToken(requestDto.getToken())
                .orElseThrow(() -> new RuntimeException("Token not found!"));

        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Your token is expired");
        }

        User user = token.getUser();
        String newPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.setPassword(newPassword);

        userRepository.save(user);
        passwordResetTokenRepository.delete(token);

        return "Password successfully reset";

    }


}
