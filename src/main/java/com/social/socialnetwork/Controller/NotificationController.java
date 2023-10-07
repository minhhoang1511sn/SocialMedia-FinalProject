package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.NotificationService;
import com.social.socialnetwork.dto.NotificationDTO;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Notification;
import com.social.socialnetwork.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/get-all-notifications")
    public ResponseEntity<?> getAllNotifications(@RequestParam String userId){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", notificationService.getAllNotificationsByUser(userId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @PutMapping("/change-status-notifications")
    public ResponseEntity<?> changeStatusNotifications(@RequestBody NotificationDTO notificationDTO){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", notificationService.changeStatusNoti(notificationDTO)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @PostMapping("/new-notifications")
    public ResponseEntity<?> newNotifications(@RequestBody NotificationDTO notificationDTO){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", notificationService.newNotificaition(notificationDTO)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
}
