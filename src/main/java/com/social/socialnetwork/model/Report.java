package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "report")
public class Report {
    @Id
    private String id;
    private String contentReport;
    @Embedded
    private User user;
    @Embedded
    private Post post;
}
