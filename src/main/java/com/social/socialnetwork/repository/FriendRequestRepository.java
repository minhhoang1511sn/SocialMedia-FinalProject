package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends MongoRepository<FriendRequest,String> {
    @Query("{'userId' : { $lt: ?0 }}")
    List<FriendRequest> getListFriendRequest(String userId);
}
