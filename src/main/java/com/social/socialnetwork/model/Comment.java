package com.social.socialnetwork.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

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
    private String content;
    @DBRef
    @Transient
    private Post post;
    private Double rate;
    private Date createTime;

}
