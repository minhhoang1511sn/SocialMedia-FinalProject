package com.social.socialnetwork.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Email may not be blank")
    private String email;
    @NotBlank(message = "Password may not be blank")
    private String password;
}

