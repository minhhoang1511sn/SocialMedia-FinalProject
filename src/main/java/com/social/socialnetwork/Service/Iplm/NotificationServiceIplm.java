package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.NotificationService;
import com.social.socialnetwork.dto.NotificationDTO;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.NotificationRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceIplm implements NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Notification> getAllNotificationsByUser() {
        User user = userRepository.findUserById(Utils.getIdCurrentUser());
        List<Notification> notifications = notificationRepository.getNotificationsByUser(user);
        return notifications;
    }

    @Override
    public Notification changeStatusNoti(NotificationDTO notificationDTO) {
        boolean check = notificationRepository.existsById(notificationDTO.getId());
        Notification notification = notificationRepository.findNotificationById(notificationDTO.getId());
        if(check)
        {
            notification.setIsRead(true);
            return notification;
        }
        else throw new AppException(500, "Notification not found");

    }

    @Override
    public Notification newNotificaition(NotificationDTO notificationDTO) {
        Notification notification = modelMapper.map(notificationDTO, Notification.class);
        User user = userRepository.findUserById(notificationDTO.getUser().getId());

        notification.setUser(user);
        notification.setCreateTime(new Date());
        notification.setIsRead(false);
        notification.setTypeNotifications(notificationDTO.getTypeNotifications());
        notification.setContent(notification.getContent());

        return notificationRepository.save(notification);
    }
}
