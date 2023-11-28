package com.social.socialnetwork.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "images")
public class Image implements Serializable {
    @Id
    private String id;
    private String imgLink;
    private PostType postType;

    public Image(Object o, String imgLink) {
        this.imgLink = imgLink;
//        this.post = post;

    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}
