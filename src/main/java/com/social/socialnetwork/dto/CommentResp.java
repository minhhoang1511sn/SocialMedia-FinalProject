package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentResp {
    private String id;
    private String content;
    private Date create_time;
    private double rate;
    private UserResp user;
    private PostResp postResp;

}
