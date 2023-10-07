package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.User;

import java.util.List;

public interface FriendService {
     Boolean isFriend(User user1, User user2);
     List<User> getUserFriends(String id);
     List<User> suggestFriend();

     void saveFriend(User userDto1, String id) throws NullPointerException;

     List MutualFriends(String id);
     boolean unFriend(String id);
     User findById(String id);
}
