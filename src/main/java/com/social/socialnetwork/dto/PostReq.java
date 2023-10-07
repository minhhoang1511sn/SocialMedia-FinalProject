package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostReq {
    private String id;
    private String user;
    private String userPost;
    private Long countLike;
    private List<Comment> commentList;
    private Image imageList;
    private String content;
    private PostType postType;
    private Date createDate;
}
