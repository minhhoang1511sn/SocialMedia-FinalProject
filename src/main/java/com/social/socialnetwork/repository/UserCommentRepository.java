package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.User;
import com.social.socialnetwork.model.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserCommentRepository extends MongoRepository<UserComment,String> {
    @Query(value = "{ 'userId' : ?0 }")
    List<UserComment> findAllByUserId(String userId);
}
