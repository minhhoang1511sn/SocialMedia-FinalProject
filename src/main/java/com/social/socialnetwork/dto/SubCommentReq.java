package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.Video;
import lombok.Data;

@Data
public class SubCommentReq {
    private String id;
    private String content;
    private String userCommentId;
    private Image image;
    private Video video;
    private String parentCommentId;
}
