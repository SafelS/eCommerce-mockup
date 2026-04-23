package com.project.ecommerce.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordRequestDto {

    @NotEmpty
    private String token;

    @NotEmpty
    @Size(min = 6, message = "Your new password has to be at least 6 characters long")
    private String newPassword;

}
