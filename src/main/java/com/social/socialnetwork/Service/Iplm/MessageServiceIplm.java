package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.MessageService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.MessageDTO;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.MessageRepository;
import com.social.socialnetwork.repository.UserMessageRepository;
import com.social.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceIplm implements MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMessageRepository userMessageRepository;
    @Override
    public Collection<MessageDTO> findAllRecentMessages(String id) {
        List<Message> all = messageRepository.findAllRecentMessages(id);
        Map<UserMessage, MessageDTO> map = new HashMap<>();
        all.forEach(m -> {
            MessageDTO messageDTO = modelMapper.map(m,MessageDTO.class);
            UserMessage user = m.getUSender().getUserId().equals(id) ? m.getUReceiver() : m.getUSender();
            map.put(user, messageDTO);
        });
        return map.values();
    }

    @Override
    public List<MessageDTO> findConversation(String userId, String companionId) {
        List<Message> all = messageRepository.findConversation(userId, companionId);
        List<MessageDTO> messages = new LinkedList<>();
        all.forEach(m -> messages.add(modelMapper.map(m,MessageDTO.class)));
        return messages;
    }

    @Override
    public MessageDTO getRecentMessage(String id) {
        Message message = messageRepository.findFirstBySenderIdOrReceiverIdOrderByIdDesc(id, id);
        MessageDTO messageDTO = modelMapper.map(message,MessageDTO.class);
        return messageDTO;
    }


    @Override
    public Message postMessage(MessageDTO messageDTO) {
        User sender = userService.getCurrentUser();
        User receiver = userRepository.findUserById(messageDTO.getReceiver().getId());
        UserMessage uSender = new UserMessage();
        UserMessage uRReceiver = new UserMessage();
        Message message = new Message();

        message.setMessage(messageDTO.getMessage());
        message.setUReceiver(uRReceiver);
        message.setUSender(uSender);
        if(sender.getImage()!=null)
            uSender.setAvatar(sender.getImage().getImgLink());
        if(receiver.getImage()!=null)
            uRReceiver.setAvatar(receiver.getImage().getImgLink());
        uSender.setFirstName(sender.getFirstName());
        uRReceiver.setFirstName(receiver.getFirstName());
        uSender.setLastName(sender.getLastName());
        uRReceiver.setLastName(receiver.getLastName());
        uSender.setUserId(sender.getId());
        uRReceiver.setUserId(receiver.getId());
        message.setCreateTime(LocalDateTime.now());
        userMessageRepository.save(uRReceiver);
        userMessageRepository.save(uSender);
        messageRepository.save(message);
        return message;
    }
}
