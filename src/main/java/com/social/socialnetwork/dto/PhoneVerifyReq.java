package com.social.socialnetwork.dto;

import lombok.Data;


@Data
public class PhoneVerifyReq {
    private String phone;
    private String code;
}