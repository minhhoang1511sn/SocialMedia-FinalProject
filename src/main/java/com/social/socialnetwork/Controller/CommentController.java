package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.CommentService;
import com.social.socialnetwork.Service.SubCommentService;
import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.dto.SubCommentReq;
import com.social.socialnetwork.model.Comment;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
public class CommentController {
    private final CommentService commentService;
    private final SubCommentService subCommentService;

    @GetMapping("/list-comment")
    public ResponseEntity<?> getListCommentByPost(@RequestParam String postid){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentService.getAllCommentByPost(postid)));
    }
    @PostMapping(value = "/comment",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postComment(@RequestBody CommentReq commentReq){
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", commentService.postComment(commentReq)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id){
        Comment comment = commentService.findById(id);
        if (commentService.deleteComment(comment.getId())) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Comment ID not exits", null));

    }
    @PutMapping("/comment")
    public ResponseEntity<?> updateComment(@RequestBody CommentReq commentReq){
        Comment commentUpdate = commentService.updateComment(commentReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentUpdate));
    }
    @PostMapping("comment/subcomment")
    public ResponseEntity<?> createSubComment(@RequestBody SubCommentReq subCommentReq) {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", subCommentService.createSubComment(subCommentReq)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @GetMapping("/subcomment/{commentParentId}")
    public ResponseEntity<?> findSubCommetByParentCommentId(@PathVariable final String commentParentId) {
        List<Comment> subComment = subCommentService.findSubCommetByParentCommentId(commentParentId);
        if (subComment != null) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", subComment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Not found", null));

    }
    @PutMapping("/comment/{parentId}")
    public ResponseEntity<?> updateSubComment(@PathVariable final String parentId,@RequestBody SubCommentReq subCommentReq){
        Comment commentUpdate = subCommentService.updateSubComment(parentId, subCommentReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",commentUpdate));
    }

}
