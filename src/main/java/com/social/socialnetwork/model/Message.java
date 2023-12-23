package com.social.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "message")
public class Message {
    @Id
    private String id;
    @Embedded
    private User sender;
    @Embedded
    private User receiver;
    private String message;
    private Date createTime;
}
