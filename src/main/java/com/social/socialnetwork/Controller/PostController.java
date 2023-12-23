package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.PostService;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.repository.PostRepository;
import com.social.socialnetwork.utils.Utils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class PostController {
    private final PostService postService;
    @PostMapping(value = "/post", consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> createPost(@ModelAttribute  PostReq postReq,@RequestParam(value = "image", required =
            false) List<MultipartFile> image,@RequestParam(value = "video",required =false) List<MultipartFile> video,@RequestParam(value = "tags", required =
        false) List<String> idTags)
            throws IOException {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", postService.createPost(postReq,image,video,idTags)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }

    }
    @GetMapping(path="/")
    public String getURLPost(HttpServletRequest httpServletRequest) {
        String link = String.valueOf(httpServletRequest.getRequestURL());
        return link;
    }
    @PutMapping(value = "/update-post", consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> updatePost(@ModelAttribute PostReq postReq,@RequestParam(value = "image", required =
            false) MultipartFile image){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", postService.updatePost(postReq,image)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }

    @PutMapping(value = "/like-post")
    public ResponseEntity<?> likePost(@RequestParam  String postId){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", postService.likePost(postId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }

    @PutMapping(value = "/unlike-post")
    public ResponseEntity<?> unLikePost(@RequestParam  String postId){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", postService.unlikePost(postId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<?> deletePost( @PathVariable String id)
    {
        if (postService.deletePost(id)) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "SubComment ID not exits", null));

    }
    @GetMapping("/posts")
    public ResponseEntity<?> listPost(@RequestParam  String userId){
        List<Post> posts = postService.getAllPost(userId);
        if(posts!=null)
        return ResponseEntity.ok(new ResponseDTO(true,"Success",posts));
        else return ResponseEntity.ok(new ResponseDTO(false,"Invalid",null));
    }

    @GetMapping("/newfeeds")
    public ResponseEntity<?> gettingNewFeeds(){
        List<Post> posts = postService.gettingPostByFriend();
        if(posts!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",posts));
        else return ResponseEntity.ok(new ResponseDTO(false,"Invalid",null));
    }

    @GetMapping("/{postId}/link")
    public ResponseEntity<String> getPostLink(@PathVariable String postId) {

        Post post = postService.findPostById(postId);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        String postLink = "localhost:5000/api/v1/posts/" + post.getId();
        return ResponseEntity.ok(postLink);
    }
}
