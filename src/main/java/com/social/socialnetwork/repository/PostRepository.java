package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.*;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {

    @Query("{ 'user' : ?0 }")
    List<Post> findAllrderByCreateDate(User user);
//    @Query(sort = "{ createDate : -1 }")
//    List<Post> findAllPostOrderByCreateDate();
    @Query("{'_id' : ?0}")
    Post getById(String id);
    @Query(value = "{ 'userId' : ?0 }", sort = "{ 'createDate' : -1 }")
    List<Post> getAllPostByUser(String userId);
}
