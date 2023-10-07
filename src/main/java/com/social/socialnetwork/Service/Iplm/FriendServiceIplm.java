package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.FriendService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.FriendRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    public List<User> getUserFriends(String id){
        User currentUser = userRepository.findUserById(id);
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();
        for (Friend friend : friendsByFirstUser) {
            friendUsers.add(userRepository.findUserById(friend.getSecondUser().getId()));
        }
        for (Friend friend : friendsBySecondUser) {
            friendUsers.add(userRepository.findUserById(friend.getFirstUser().getId()));
        }
        return friendUsers;
    }

    @Override
    public List<User> suggestFriend() {
        List<User> user = userRepository.findAll();
        User curU = userRepository.findUserById(Utils.getIdCurrentUser());
        List<User> suggestFriend = new ArrayList<>();
        user.forEach(
                u->{
                    if(!isFriend(curU,u) && u!=curU)
                    {
                        suggestFriend.add(u);
                    }
                }
        );
        return suggestFriend;
    }

    @Override
    public void saveFriend(User userDto1, String id) throws NullPointerException{
        Friend friend = new Friend();
        User UserFriend = userRepository.findUserById(id);
        User curUser = userRepository.findUserByEmail(userDto1.getEmail());
        User friendUser = userRepository.findUserByEmail(UserFriend.getEmail());
        User firstuser = curUser;
        User seconduser = friendUser;
//        if(curUser.getId() > friendUser.getId()){
//            firstuser = friendUser;
//            seconduser = curUser;
//        }
        if( !(friendRepository.existsByFirstUserAndSecondUser(firstuser,seconduser)) ){
            friend.setAccept(true);
            friend.setCreatedDate(new Date());
            friend.setFirstUser(firstuser);
            friend.setSecondUser(seconduser);
            friendRepository.save(friend);
        }
    }
    @Override
    public Boolean isFriend(User user1, User user2){
        User UserFriend = userRepository.findUserById(user2.getId());
        User curUser = userRepository.findUserByEmail(user1.getEmail());
        User friendUser = userRepository.findUserByEmail(UserFriend.getEmail());
        User firstuser = curUser;
        User seconduser = friendUser;
//        if(curUser.getId() > UserFriend.getId()){
//            firstuser = friendUser;
//            seconduser = curUser;
//        }
        Friend check = friendRepository.findByFirstUserAndSecondUser(firstuser,seconduser);
            if(check!=null)
            return  true;
            else return false;
    }



    @Override
    public List MutualFriends(String id) {
        List<User> user1 = getUserFriends(id);
        List<User> user2 = getUserFriends(Utils.getIdCurrentUser());
        if(user1.size()> user2.size())
        {
            user1.retainAll(user2);
            return user1;
        }
        else  if(user1.size() == 0 || user2.size() ==0){
            return new ArrayList();
        }
        else {
            user2.retainAll(user1);
            return user2;
        }
    }



    @Override
    public boolean unFriend(String id) {
        User UserFriend = userRepository.findUserById(id);
        User curUser = userRepository.findUserById(Utils.getIdCurrentUser());
        User friendUser = userRepository.findUserByEmail(UserFriend.getEmail());
        User firstuser = curUser;
        User seconduser = friendUser;
//        if(curUser.getId() > UserFriend.getId()){
//            firstuser = friendUser;
//            seconduser = curUser;
//        }
        if(friendRepository.existsByFirstUserAndSecondUser(firstuser,seconduser))
        {
           Friend deleteRelation =  friendRepository.findByFirstUserAndSecondUser(firstuser,seconduser);
           deleteRelation.setFirstUser(null);
           deleteRelation.setSecondUser(null);
           friendRepository.save(deleteRelation);
           friendRepository.deleteById(deleteRelation.getId());
            return true;
        }
        else
        return false;
    }
}
