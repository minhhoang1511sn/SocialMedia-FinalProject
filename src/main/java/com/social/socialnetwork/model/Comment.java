package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "comments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private String id;
    @Embedded
    private UserComment userComment;
    @Embedded
    private Page page;
    private String content;
    @DBRef
    @Transient
    private Post post;
    private Double rate;
    private Date createTime;
    @DBRef
    @Transient
    private Comment parentComment;
    private Long numReply;
    @DBRef
    @Transient
    private Image image;
    @DBRef
    @Transient
    private Video video;

}
