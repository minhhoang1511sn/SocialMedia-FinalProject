package com.social.socialnetwork.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordDTO {
    private String email;
    private String verifyCode;
    private String oldPassword;
    private String newPassword;

}
