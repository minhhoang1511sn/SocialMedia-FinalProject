package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.CommentService;
import com.social.socialnetwork.Service.SubCommentService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.dto.SubCommentReq;
import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.model.UserComment;
import com.social.socialnetwork.repository.CommentRepository;
import com.social.socialnetwork.repository.UserCommentRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.webjars.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class SubCommentServiceIplm implements SubCommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final UserCommentRepository userCommentRepository;
    @Override
    public Comment findById(String id) {
        Optional<Comment> subComment = commentRepository.findById(id);
        return subComment.orElse(null);
    }

    @Override
    public Comment createSubComment(SubCommentReq subCommentReq) {
        if (subCommentReq == null) {
            throw new IllegalArgumentException("SubCommentReq can not allow null");
        }

        if (subCommentReq.getParentCommentId() == null) {
            throw new IllegalArgumentException("Comment id can not allow null");
        }
        if (subCommentReq.getContent() == null) {
            throw new IllegalArgumentException("Content can not allow null");
        }
        User user = userService.findById(Utils.getIdCurrentUser());
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        Comment comment = commentService.findById(subCommentReq.getParentCommentId());
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }
        UserComment userComment = new UserComment();
        userComment.setComment(comment);
        userComment.setAvatar(String.valueOf(user.getImage()));
        userComment.setFirstName(user.getFirstName());
        userComment.setLastName(user.getLastName());
        userComment.setUserId(userComment.getUserId());
        userCommentRepository.save(userComment);
        Comment subComment = Comment.builder()
                .userComment(userComment)
                .parentComment(comment)
                .createTime(new Date())
                .numReply(0L)
                .content(subCommentReq.getContent())
                .build();
        return commentRepository.save(subComment);
    }

    @Override
    public Comment updateSubComment(String id, SubCommentReq subCommentReq) {
        Comment subCommentParentUpdate = findById(id);
        Comment SubCommentUpdate = null;
        if (subCommentParentUpdate == null) {
            return null;
        }
        if (!StringUtils.isEmpty(subCommentReq.getContent())) {
             SubCommentUpdate = findById(subCommentReq.getId());
            SubCommentUpdate.setContent(subCommentReq.getContent());

        }

        return commentRepository.save(SubCommentUpdate);
    }


    @Override
    public List<Comment> findSubCommetByParentCommentId(String commentParentId) {
        List<Comment> subComments = commentRepository.findAllByParentComment(commentParentId);
        return subComments;
    }
}
