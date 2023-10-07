package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_post")
public class UserPost {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String userId;
    private String avatar;
}
