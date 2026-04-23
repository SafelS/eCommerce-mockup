package com.project.ecommerce.controller;

import com.project.ecommerce.dto.*;
import com.project.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto);
    }

    @PostMapping("/register-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto registerAdmin(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return authService.registerAdmin(registerRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDto loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto requestDto){
        authService.forgotPassword(requestDto);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public String resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto){
        return authService.resetPassword(requestDto);
    }




}
