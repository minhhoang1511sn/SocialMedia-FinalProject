package com.social.socialnetwork.dto;

import lombok.Data;

@Data
public class ReportReq {
    private String contentReport;
    private String userId;
    private String postId;
}
