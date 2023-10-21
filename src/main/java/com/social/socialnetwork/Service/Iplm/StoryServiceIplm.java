package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.StoryService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.StoryReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.StoryRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class StoryServiceIplm implements StoryService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final StoryRepository storyRepository;
    @Override
    public Story createStory(StoryReq storyReq, MultipartFile images) {
        String idCurrentUser = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(idCurrentUser);
        if(check){
            Story story = new Story();
            User user = userService.findById(idCurrentUser);
           story.setCountLike(0);
           story.setCreateTime(new Date());
           story.setEnabled(true);
           story.setCountUserWatched(0);
           story.setPostType(PostType.valueOf(storyReq.getPostType()));
            if(storyReq.getImage()!=null)
            {
                Image image = new Image();
                image.setPostType(PostType.valueOf(storyReq.getPostType()));
                image.setImgLink(storyReq.getImage());
                story.setImage(image);
            }
            if(storyReq.getVideo()!=null)
            {
                Video video = new Video();
                video.setPostType(PostType.valueOf(storyReq.getPostType()));
                video.setVideoLink(storyReq.getVideo());
                story.setVideo(video);
            }
            storyRepository.save(story);
            return story;
        } else {
            throw new AppException(404, "Story not exits.");
        }
    }
}
