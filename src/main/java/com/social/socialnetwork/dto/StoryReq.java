package com.social.socialnetwork.dto;

import lombok.Data;

import java.util.Date;
@Data
public class StoryReq {
    private long countLike;
    private String postType;
    private String user;
    private boolean enabled;
    private String image;
    private String video;
    private Date createTime;
    private long countUserWatched;
}
