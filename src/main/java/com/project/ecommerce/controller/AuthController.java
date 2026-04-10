package com.project.ecommerce.controller;

import com.project.ecommerce.dto.AuthResponseDto;
import com.project.ecommerce.dto.LoginRequestDto;
import com.project.ecommerce.dto.RegisterRequestDto;
import com.project.ecommerce.service.AuthService;
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
    public AuthResponseDto registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto);
    }

    @PostMapping("/register-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto registerAdmin(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.registerAdmin(registerRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDto loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }




}
