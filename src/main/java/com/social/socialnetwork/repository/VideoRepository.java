package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends MongoRepository<Video,String> {
    @Query("{'user': ?0}")
    List<Video> getAllVideoByUser(User user);
    @Query("{'post': ?0}")
    List<Video> getAllVideoByPost(Post post);
    @Query("{'page': ?0}")
    List<Video> getAllVideoByPage(Page page);
}
