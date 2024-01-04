package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.MessageService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.MessageDTO;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.model.Message;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.MessageRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class MessageControllerq {
    private final MessageService messageService;
    private final UserService userService;
    @PostMapping(value = "/send-message",consumes = {"multipart/form-data"})
    public ResponseEntity<?> sendMessage(@ModelAttribute MessageDTO messageDTO, @Parameter(
        description = "Files to be uploaded",
        content =  @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    @RequestParam(value = "image", required =false) MultipartFile image){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", messageService.postMessage(messageDTO,image)));
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

    @GetMapping("/find-all-messages")
    public ResponseEntity<?> getAllMessages(){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", userService.findAllUserMessages()));
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
