package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.FriendService;
import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.FriendRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceIplm implements FriendService {


  private final FriendRepository friendRepository;

  private final UserRepository userRepository;


  @Override
  public User findById(String id) {
    Optional<User> user = userRepository.findById(id);
    return user.orElse(null);
  }


  //get danh sách friend của user
  @Override
  public List<User> getUserFriends(String id) {
    User currentUser = userRepository.findUserById(id);
    List<String> friends = new ArrayList<>();
    List<User> friendUsers = new ArrayList<>();
    if(currentUser.getUserFriend()!=null)
    {
      friends = currentUser.getUserFriend();
      for (String friend : friends) {
        friendUsers.add(userRepository.findUserById(friend));
      }
    }

    return friendUsers;
  }

  @Override
  public List<User> getUserRequestFriends(String id) {
    User user = userRepository.findUserById(id);
    List<User> userReq = new ArrayList<>();
    List<String> userReqId = user.getUserRequest();

    for (int i = 0; i < userReqId.size(); i++) {
      userReq.add(userRepository.findUserById(userReqId.get(i)));
    }
    return userReq;
  }


  @Override
  public List<User> suggestFriend() {
    List<User> user = userRepository.findAll();
    User curU = userRepository.findUserById(Utils.getIdCurrentUser());
    List<User> suggestFriend = new ArrayList<>();
    user.forEach(
        u -> {
          if (isFriend(curU, u)== null && u != curU) {
            suggestFriend.add(u);
          }
        }
    );
    return suggestFriend;
  }

  @Override
  public void saveFriend(User userDto1, String id) throws NullPointerException {

    User userFriend = userRepository.findUserById(id);
    User curUser = userRepository.findUserById(Utils.getIdCurrentUser());
    List<String> friendUserFriend = new ArrayList<>();
    List<String> curUserFriend = new ArrayList<>();
    if(userFriend!=null && userFriend.getUserFriend()!=null)
    {
      friendUserFriend = userFriend.getUserFriend();
    }

    if(curUser!=null && curUser.getUserFriend()!=null)
    {
      curUserFriend = curUser.getUserFriend();
    }
    if(friendUserFriend.contains(curUser.getId()) && curUserFriend.contains(userFriend.getId()))
    {

    }

      List<String> userFrReq = userFriend.getUserRequest();
      List<String> userReq = curUser.getUserRequest();
//      boolean check1 = userFrReq.contains(curUser.getId());
//      boolean check2 = userReq.contains(UserFriend.getId());
//      if (!check1 && !check2) {
//        List<String> UserFriendReq = UserFriend.getUserRequest();
//        UserFriendReq.add(curUser.getId());
//        UserFriend.setUserRequest(UserFriendReq);
//        userRepository.save(UserFriend);
//      } else {
//        List<String> curFriend = new ArrayList<>();
//        List<String> uFrFriend = new ArrayList<>();
//        List<String> curUserReq = curUser.getUserRequest();
//        if (curUser.getUserFriend() != null) {
//          curUser.setUserFriend(curUser.getUserFriend());
//        }
//        if (UserFriend.getUserFriend() != null) {
//          UserFriend.setUserFriend(UserFriend.getUserFriend());
//        }
//        curUserReq.remove(UserFriend.getId());
//        curFriend.add(UserFriend.getId());
//        uFrFriend.add(curUser.getId());
//        curUser.setUserFriend(curFriend);
//        UserFriend.setUserFriend(uFrFriend);
//        curUser.setUserRequest(curUserReq);
//        userRepository.save(curUser);
//        userRepository.save(UserFriend);
//      }
//    }
  }

  @Override
  public boolean DontAccept(String friendId) {
    User curUser = userRepository.findUserById(Utils.getIdCurrentUser());
    if(curUser.getUserRequest()!=null)
    {
      List<String> userReq = curUser.getUserRequest();
      if(userReq.contains(friendId))
      {
        userReq.remove(friendId);
        curUser.setUserRequest(userReq);
        userRepository.save(curUser);
        return true;
      }
    }
    return false;
  }

  @Override
  public String isFriend(User user1, User user2) {
    User UserFriend = userRepository.findUserById(user2.getId());
    User curUser = userRepository.findUserById(user1.getId());
    List<String> curUserFriendReq = new ArrayList<>();
    List<String> userFriendReq = new ArrayList<>();
    List<String> userFriend = new ArrayList<>();
    List<String> curUserFriend = new ArrayList<>();

    if(curUser!=null){
      if(curUser.getUserFriend()!=null)
      {
        curUserFriend = curUser.getUserFriend();
      }

    }
    String user2Nm = "";
    if(UserFriend!=null){
      user2Nm = UserFriend.getFirstName() + " " +UserFriend.getLastName();
      if(UserFriend.getUserFriend()!=null)
      {
        userFriend = UserFriend.getUserFriend();
      }
    }

      assert curUser != null;
      if ( userFriend.contains(curUser.getId()) && curUserFriend.contains(UserFriend.getId())) {
          return "you and "+user2Nm+" is a friend";
      }
      else {
        if(curUser.getUserRequest() != null)
         curUserFriendReq = curUser.getUserRequest();
        if(UserFriend!=null && UserFriend.getUserRequest()!=null)
          userFriendReq = UserFriend.getUserRequest();
        if (!userFriendReq.contains(curUser.getId()) && !curUserFriendReq.contains(UserFriend.getId())) {
          return "you and "+user2Nm+" don't have friend request";
        } else {
          if (userFriend.contains(curUser.getId()) && !curUserFriend.contains(UserFriend.getId())) {
            return "you have been sent friend request to  "+user2Nm;
          } else {
            return  user2Nm+" sent friend request to you";
          }
        }
      }
  }


  @Override
  public List MutualFriends(String id) {
    List<User> user1 = getUserFriends(id);
    List<User> user2 = getUserFriends(Utils.getIdCurrentUser());
    if (user1.size() > user2.size()) {
      user1.retainAll(user2);
      return user1;
    } else if (user1.size() == 0 || user2.size() == 0) {
      return new ArrayList();
    } else {
      user2.retainAll(user1);
      return user2;
    }
  }


  @Override
  public boolean unFriend(String id) {
    User UserFriend = userRepository.findUserById(id);
    User curUser = userRepository.findUserById(Utils.getIdCurrentUser());

    if (isFriend(UserFriend, curUser)!=null) {
      List<String> curUserFr = curUser.getUserFriend();
      List<String> friendUserFr = UserFriend.getUserFriend();
      curUserFr.remove(UserFriend.getId());
      friendUserFr.remove(curUser.getId());
      curUser.setUserFriend(curUserFr);
      UserFriend.setUserFriend(friendUserFr);
      userRepository.save(curUser);
      userRepository.save(UserFriend);
      return true;
    }
    return false;
  }
}
