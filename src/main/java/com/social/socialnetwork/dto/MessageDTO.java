package com.social.socialnetwork.dto;


import com.social.socialnetwork.model.User;
import com.social.socialnetwork.model.UserMessage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class MessageDTO {
    private Date time;

    @NotNull
    @Size(min=3, max = 3000)
    private String message;
    private UserMessage sender;
    private UserMessage receiver;
}
