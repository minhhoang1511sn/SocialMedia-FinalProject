package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.CommentService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.dto.PostResp;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.CommentRepository;
import com.social.socialnetwork.repository.PostRepository;
import com.social.socialnetwork.repository.UserCommentRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceIplm implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserCommentRepository userCommentRepository;

    @Override
    public Comment findById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }
    @Override
    public Comment postComment(CommentReq commentReq) {
        String userId = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(userId) && postRepository.existsById(commentReq.getPostId());
        UserComment userComment = new UserComment();
        if(check){
                Comment comment = new Comment();
                comment.setRate(0.0);
                comment.setContent(commentReq.getContent());
                User user = userService.findById(userId);
                Post post = postRepository.findById(commentReq.getPostId()).orElse(null);
                comment.setPost(post);
                comment.setCreateTime(new Date());
                if(user.getImage()!=null)
                userComment.setAvatar(user.getImage().getImgLink());
                userComment.setFirstName(user.getFirstName());
                userComment.setLastName(user.getLastName());
                userComment.setUserId(user.getId());
                userCommentRepository.save(userComment);
                comment.setUserComment(userComment);
                commentRepository.save(comment);
                return comment;
        } else {
            throw new AppException(404, "Post or Comment not exits.");
        }
    }


    @Override
    public List<Comment> getAllCommentByPost(String postid) {
        Post post = postRepository.getById(postid);
        List<Comment> commentList = commentRepository.getCommentByPost(post);
        return commentList;
    }

    @Override
    public Comment updateComment(CommentReq commentReq) {
        String userId = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(userId);
        if(check){
            Comment commentUpdate = findById(commentReq.getId());
            List<UserComment> uc = userCommentRepository.findAllByUserId(userId);
            UserComment u = uc.get(0);
            if(commentUpdate != null) {
               commentUpdate.setContent(commentReq.getContent());
               commentUpdate.setUserComment(u);
               commentUpdate.setRate(commentReq.getRate());
                return commentUpdate;
            } else throw new AppException(404, "Comment ID not found");
        }
        else throw new AppException(500, "User not found");

    }

    @Override
    public boolean deleteComment(String id) {
        Comment comment = commentRepository.findById(id).orElse(null);
            if(comment != null){
                commentRepository.deleteById(id);
                return  true;
            }
            else{
                throw new AppException(404, "Comment ID not found");}

    }

}
