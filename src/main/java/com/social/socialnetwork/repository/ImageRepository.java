package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<Image,String> {
    @Query("{'user': ?0}")
    List<Image> getAllImageByUser(User user);
    @Query("{'post': ?0}")
    List<Image> getAllImageByPost(Post post);
}
