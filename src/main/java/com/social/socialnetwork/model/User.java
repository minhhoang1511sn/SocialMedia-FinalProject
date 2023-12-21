package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User implements UserDetails {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;

    @Embedded
    private Image image;
    @Embedded
    private Image background;
    @Embedded
    private Page page;
    private Date  birthday;
    private String gender;
    @DBRef
    private List<Video> videos;
    @DBRef
    private List<Image> images;
    @DBRef
    private List<Post> posts;
    @DBRef
    private List<Page> pagefollowed;
    private List<String> userFriend;
    private List<String> userRequest;
    private List<String> userRespone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean Enabled;
    private Boolean isActive;
    @Embedded
    @Transient
    private ConfirmationCode confirmationCode;
    @NotBlank(message = "Password may not be blank")
    private String phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getUsername() {
        return email!=null ? email : phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
