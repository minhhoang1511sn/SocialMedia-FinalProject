package com.social.socialnetwork.Service;

import com.social.socialnetwork.model.User;

import java.util.List;

public interface FriendService {
     String isFriend(User user1, User user2);
     List<User> getUserFriends(String id);

     List<User> getUserRequestFriends(String id);

     List<User> suggestFriend();

     boolean saveFriend(User userDto1, String id) throws NullPointerException;

     boolean DontAccept(String friendId);

     List MutualFriends(String id);
     boolean unFriend(String id);
     User findById(String id);
}
