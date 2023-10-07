package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.PostType;

import lombok.Data;

import java.util.List;

@Data
public class PostResp {
    private String id;
    private String user;
    private Long countLike;
    private List<Comment> commentList;
    private Image imageList;
    private String content;
    private PostType postType;
}
