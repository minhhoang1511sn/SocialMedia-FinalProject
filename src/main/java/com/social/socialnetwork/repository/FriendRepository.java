package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRepository extends MongoRepository<Friend,String> {

    boolean existsByFirstUserAndSecondUser(User first,User second);
    @Query("{'email' : ?0}")
    List<Friend> findByFirstUser(User user);
    @Query("{'email' : ?0}")
    List<Friend> findBySecondUser(User user);
    @Query("{'firstName' : ?0,'lastName' : ?1}")
    Friend findByFirstUserAndSecondUser(User first,User second);

}
