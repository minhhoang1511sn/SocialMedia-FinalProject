package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public interface UserService {
     User findById(String id);
     List<User> findAllUser();
     User updateUser(UserReq userReq);
     User findUserByEmail(String email);
     User findUserByPhone(String phone);
     ArrayList<Object> findUserByUserName(String query);
     void changePassword(User user, String newPassword);
     ConfirmationCode SendVerifyCode(String email);
     boolean disabledUser(String id);
     boolean enabledUser(String id);
     boolean checkIfValidOldPassword(User user, String oldPassword);
     String upAvartar(MultipartFile file)  throws IOException ;
     User getCurrentUser();
     List<Image> getimgByUser(String userId);
     List<Image> getAllImageByUser(String userId);

     List<Post> getPostReported();
     List<Page> getPageReported();
     List<Comment> getCommentReported();
     boolean disabledPost(PostReq postReq);

     boolean disabledPage(PageReq pageReq);
     boolean disabledComment(CommentReq commentReq);
     boolean deletePost(PostReq postReq);

     boolean deletePage(PageReq pageReq);

      void saveUser(User user);

      void disconnect(User user);

      List<User> findConnectedUsers();

     String upBackGround(MultipartFile file)  throws IOException ;

     List<User> findActiveFriend();
}
