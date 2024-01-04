package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.MessageService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.MessageDTO;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.ImageRepository;
import com.social.socialnetwork.repository.MessageRepository;
import com.social.socialnetwork.repository.UserMessageRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.repository.VideoRepository;
import com.social.socialnetwork.utils.Utils;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceIplm implements MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMessageRepository userMessageRepository;
    private final CloudinaryUpload cloudinaryUpload;
    private final ImageRepository imageRepository;
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

    public String sendImage(MultipartFile file) throws IOException {
        String id = Utils.getIdCurrentUser();
        User user = userRepository.findUserById(id);
        if (user == null)
            throw new AppException(404, "User ID not found");
        Image imgUrl = new Image();
        imgUrl.setImgLink(cloudinaryUpload.uploadImage(file,null));
        imgUrl.setPostType(PostType.FRIEND);
        imageRepository.save(imgUrl);

        return imgUrl.getImgLink();
    }
    @Override
    public Message postMessage(MessageDTO messageDTO, MultipartFile image) throws IOException {
        User sender = userService.getCurrentUser();
        User receiver = userRepository.findUserById(messageDTO.getReceiverId());
        UserMessage uSender = new UserMessage();
        UserMessage uRReceiver = new UserMessage();
        Message message = new Message();

        message.setMessage(messageDTO.getMessage());
        message.setUReceiver(uRReceiver);
        message.setUSender(uSender);

        if(image!=null)
        {
            Image img = new Image();
            img.setImgLink(sendImage(image));

            message.setImg(img);
        }

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
        message.setCreateTime(new Date());
        userMessageRepository.save(uRReceiver);
        userMessageRepository.save(uSender);
        messageRepository.save(message);
        return message;
    }
}
