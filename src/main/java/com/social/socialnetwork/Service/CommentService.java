package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.dto.CommentResp;
import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    Comment findById(String id);
    Comment postComment(CommentReq commentReq);
    List<Comment> getAllCommentByPost(String postid);
    Comment updateComment(CommentReq commentReq);
    boolean deleteComment(String id);
}
