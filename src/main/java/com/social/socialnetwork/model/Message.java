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
    @Transient
    private User sender;
    @Embedded
    @Transient
    private User receiver;
    @Embedded
    private UserMessage uSender;
    @Embedded
    private UserMessage uReceiver;
    private String message;
    private LocalDateTime createTime;
}
