package com.social.socialnetwork.dto;

import lombok.Data;

@Data
public class MailRequest {
    private String firstName;
    private String lastName;
    private String to;
    private String from;
    private String subject;
}
