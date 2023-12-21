package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private Long countLike;
    private String content;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    @Embedded
    private UserPost userPost;
    @Embedded
    private Page page;
    @Field("userTags")
    private List<User> usertags;
    @Field("comments")
    private List<Comment> comments;
    @Field("images")
    private List<Image> images;
    @Field("videos")
    private List<Video> videos;
    private Date createDate;
    private boolean enabled;
    private String feeling;
    private String emoji;
}
