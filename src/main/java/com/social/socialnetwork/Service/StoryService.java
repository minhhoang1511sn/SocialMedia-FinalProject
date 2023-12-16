package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.StoryReq;
import com.social.socialnetwork.model.Story;
import com.social.socialnetwork.model.StoryStored;
import org.springframework.web.multipart.MultipartFile;

public interface StoryService {
    Story createStory(StoryReq storyReq, MultipartFile images);

    boolean disabledStory(StoryReq storyReq);
}
