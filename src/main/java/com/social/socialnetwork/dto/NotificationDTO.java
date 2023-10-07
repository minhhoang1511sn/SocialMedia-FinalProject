package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.TypeNotifications;
import com.social.socialnetwork.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationDTO {
    private String id;
    private User user;
    private String content;
    private TypeNotifications typeNotifications;
    private Date createTime;
    private Boolean isRead;

}
