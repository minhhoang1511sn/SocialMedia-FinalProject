package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.MessageService;
import com.social.socialnetwork.dto.MessageDTO;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.model.Message;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.MessageRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class MessageController {
    private final MessageService messageService;
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", messageService.postMessage(messageDTO)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @GetMapping("/get-recent-message")
    public ResponseEntity<?> getRecentMessage(@RequestParam String userId){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", messageService.getRecentMessage(userId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @GetMapping("/find-all-recent-message")
    public ResponseEntity<?> getAllRecentMessage(@RequestParam String userId){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", messageService.findAllRecentMessages(userId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @GetMapping("/find-conversation")
    public ResponseEntity<?> getConversation(@RequestParam String userId){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", messageService.findConversation(Utils.getIdCurrentUser(),userId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
}
