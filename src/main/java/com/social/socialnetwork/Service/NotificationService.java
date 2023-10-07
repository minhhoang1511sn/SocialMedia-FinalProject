package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.NotificationDTO;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Notification;
import com.social.socialnetwork.model.User;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotificationsByUser(String userId);
    Notification changeStatusNoti(NotificationDTO notificationDTO);
    Notification newNotificaition(NotificationDTO notificationDTO);
}
