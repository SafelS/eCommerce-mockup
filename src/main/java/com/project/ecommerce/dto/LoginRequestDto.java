package com.project.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Please enter your e-mail")
    @Email(message = "Please enter an valid e-mail")
    private String email;

    @NotBlank(message = "Please enter your password")
    private String password;
}
