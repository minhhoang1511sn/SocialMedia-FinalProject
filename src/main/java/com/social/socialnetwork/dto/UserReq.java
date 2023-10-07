package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.Role;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserReq {
    private String id;
    private String address;
    private String gender;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthday;
    private Role role;
    private Boolean Enabled;
    private String email;
    @NotBlank(message = "Password may not be blank")
    private String phone;

}
