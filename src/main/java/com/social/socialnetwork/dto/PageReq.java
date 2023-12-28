package com.social.socialnetwork.dto;


import lombok.Data;

import java.util.Date;
@Data

public class PageReq {
    private String id;
    private Long countMember;
    private String admin;
    private String pageName;
    private String introduce;
    private Date createTime;
    private Boolean enabled;
    private String category;
    private String contact;
}
