package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.model.Video;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;



public interface PostService {
     Post  createPost(PostReq postReq,  List<MultipartFile> images,  List<MultipartFile> video,List<String> tagsId);
     Post findById(String id);
     boolean deletePost(String id);
     List<Post> getAllPost(String id);
     List<Post> getAllPostByPageId(String id);

    List<Post> getAllPostByUserNotPage(String id);

    Post updatePost(PostReq postReq, List<MultipartFile> images, List<MultipartFile> video, List<String> tagsId);
     Image uploadImage(String postId, MultipartFile images) ;
     Video uploadVideo(String postId, MultipartFile videos) ;
     List<Post> gettingPostByFriend();
     List<Post> getAllPostByUser(String userId);

     Post findPostById(String id);

     Post likePost(String id);

     Post unlikePost(String id);

}
