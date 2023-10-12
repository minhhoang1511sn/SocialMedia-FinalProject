package com.social.socialnetwork.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "posts")
public class Story {
    private String id;
    private long countLike;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    @Embedded
    private User user;
    private boolean enabled;
    @Embedded
    private Image image;
    @Embedded
    private Video video;
    private Date createTime;
    private long countUserWatched;
}
