package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.MessageDTO;
import com.social.socialnetwork.model.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface MessageService {
    Collection<MessageDTO> findAllRecentMessages(String id);

    List<MessageDTO> findConversation(String userId, String companionId);

    MessageDTO getRecentMessage(String id);

    Message postMessage(MessageDTO messageDTO);
}
