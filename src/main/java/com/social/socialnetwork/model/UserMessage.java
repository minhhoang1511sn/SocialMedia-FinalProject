package com.social.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_message")
public class UserMessage {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String userId;
    private String avatar;
    private String message;
    private Date createTime;
}
