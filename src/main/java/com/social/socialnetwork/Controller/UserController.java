package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.FriendService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.PasswordDTO;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final FriendService friendService;
    private final UserRepository userRepository;
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(){
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(new ResponseDTO(true,"Success",user));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") String id){
        User user = userService.findById(id);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",user));
    }
    @GetMapping("/alluser")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",userService.findAllUser()));
    }
    @GetMapping("/search-user")
    public ResponseEntity<?> getUserByName(@RequestParam("query") String query){

        List<User> user = userService.findUserByUserName(query);
        return ResponseEntity.ok().body(user);
    }
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserReq userReq){
        User usersUpdate = userService.updateUser(userReq);
        if (usersUpdate!= null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",usersUpdate));
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"User ID not exits",null));

    }
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDTO passwordDTO){
        User user = userService.findUserByEmail(passwordDTO.getEmail());
        if(!userService.checkIfValidOldPassword(user,passwordDTO.getOldPassword())) {
            return ResponseEntity.ok().body(new ResponseDTO(false,"Invalid Old Password",
                    null));
        }
        userService.changePassword(user, passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new ResponseDTO(true,"Password Changed Successfully",
                null));
    }
    @GetMapping("/list-Friend")
    public ResponseEntity<?> getFriendList(@RequestParam("userId") String userId){
        List<User> friendList = friendService.getUserFriends(userId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",friendList));
    }
    @GetMapping("/is-Friend")
    public ResponseEntity<?> getIsFriend(@RequestParam("friendId") String friendId){
        User curUser = userRepository.findUserById(Utils.getIdCurrentUser());
        User friend = userRepository.findUserById(friendId);
        Boolean isFriend = friendService.isFriend(curUser,friend);
        if(isFriend){
            return ResponseEntity.ok().body(new ResponseDTO(true,"Success",friend));       }
        else {
            return ResponseEntity.ok().body(new ResponseDTO(false,"User isn't friend",null));}
    }
    @GetMapping("/mutual-friends")
    public ResponseEntity<?> MutualFriends(@RequestParam("userId") String userId){

        List<User> user = friendService.MutualFriends(userId);
        return ResponseEntity.ok().body(user);
    }
    @PostMapping("/add-friend")
    public ResponseEntity<?> addFriend( @RequestParam("friendId")String friendId){
        User  currentUser = userService.findById(Utils.getIdCurrentUser());
        friendService.saveFriend(currentUser,friendId);
        return ResponseEntity.ok("Friend added successfully");
    }
    @DeleteMapping("/un-friend")
    public ResponseEntity<?> unFriend( @RequestParam("friendId")String friendId){
        if (friendService.unFriend(friendId)){
            return ResponseEntity.ok(new ResponseDTO(true, "Success", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Friend id not exixts",null));

    }
    @PutMapping(value = "/user/avatar",consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> upAvatar(@Parameter(
                description = "Files to be uploaded",
            content =  @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    )
                                      @RequestParam(value = "image", required =
                                              false) MultipartFile file) throws IOException {
        String url = userService.upAvartar(file);

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                url));
    }
    @GetMapping("/images")
    public ResponseEntity<?> getImages(@RequestParam("userId") String userId){
        List<Image> images = userService.getimgByUser(userId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",images));
    }
    @GetMapping("/suggest-friend")
    public ResponseEntity<?> getSuggestFriend(){
        List<User> suggestFriend = friendService.suggestFriend();
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",suggestFriend));
    }
}
