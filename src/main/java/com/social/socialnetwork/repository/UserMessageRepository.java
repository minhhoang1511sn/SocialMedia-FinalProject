package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends MongoRepository<UserMessage,String> {

    List<UserMessage> findAllByUserId(String id);
    UserMessage findUserMessageByUserId(String userId);
}