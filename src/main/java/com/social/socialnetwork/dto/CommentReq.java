package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.*;
import lombok.Data;

@Data
public class CommentReq {
    private String id;
    private String content;
    private Double rate;
    private String postId;
    private String userCommentId;
    private Image image;
}
