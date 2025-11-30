package com.microtech.SmartShop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientCreateDto {
    @NotBlank
    private String nom;
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
