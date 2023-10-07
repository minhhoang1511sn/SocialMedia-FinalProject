package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notification")
public class Notification {
    @Id
    private String id;
    @Embedded
    private User user;
    private String content;
    @Enumerated(EnumType.STRING)
    private TypeNotifications typeNotifications;
    private Date createTime;
    private Boolean isRead;

}
