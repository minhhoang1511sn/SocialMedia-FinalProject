package com.social.socialnetwork.model;

import jakarta.persistence.Embedded;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "page")
public class Page {
    private String id;
    private Long countMember;
    private String admin;
    @Embedded
    private Image avatar;
    @Embedded
    private Image background;
    private String pageName;
    private String introduce;
    private String contact;
    private String category;
    @DBRef
    private List<Post> posts;
    @DBRef
    private List<Image> images;
    @DBRef
    private List<Video> videos;
    private List<String> followers;
    private Date  createTime;
    private boolean enabled;
}
