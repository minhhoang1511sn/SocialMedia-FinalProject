package com.social.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReqest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean enabled;
    private String phone;
}
