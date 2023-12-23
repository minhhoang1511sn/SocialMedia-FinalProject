package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.ChatMessageService;
import com.social.socialnetwork.model.ChatMessage;
import com.social.socialnetwork.model.ChatNotification;
import com.social.socialnetwork.model.Message;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.MessageRepository;
import com.social.socialnetwork.repository.UserRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class ChatController {

  private final SimpMessagingTemplate messagingTemplate;
  private final ChatMessageService chatMessageService;

  @MessageMapping("/chat")
  public void processMessage(@Payload ChatMessage chatMessage) {
    ChatMessage savedMsg = chatMessageService.save(chatMessage);
    messagingTemplate.convertAndSendToUser(
        chatMessage.getRecipientId(), "/queue/messages",
        new ChatNotification(
            savedMsg.getId(),
            savedMsg.getSenderId(),
            savedMsg.getRecipientId(),
            savedMsg.getContent()
        )
    );
  }

  @GetMapping("/messages/{senderId}/{recipientId}")
  public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
      @PathVariable String recipientId) {
    return ResponseEntity
        .ok(chatMessageService.findChatMessages(senderId, recipientId));
  }
}

