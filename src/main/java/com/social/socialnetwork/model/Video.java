package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "videos")
public class Video implements Serializable {
    @Id
    private String id;
    private String videoLink;
    private PostType postType;

    public Video(Object o, String videoLink) {
        this.videoLink = videoLink;
//        this.post = post;

    }

    public String getvideoLink() {
        return videoLink;
    }

    public void setVideoLink(String imgLink) {
        this.videoLink = imgLink;
    }
}
