package com.social.socialnetwork.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "page_post")
public class PagePost {
  @Id
  private String id;
  private String pageId;
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
  @Field("posts")
  private List<Post> posts;
  @Field("images")
  private List<Image> images;
  @Field("videos")
  private List<Video> videos;
  private Date createTime;
  private boolean enabled;
}
