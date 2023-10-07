package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPostRepository extends MongoRepository<UserPost,String> {
    @Query(value = "{ 'userId' : ?0 }")
    List<UserPost> findAllByUserId(String userId);
}
