package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.CommentService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.*;
import com.social.socialnetwork.utils.Utils;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final PageRepository pageRepository;
  private final ReportRepository reportRepository;

  @Override
  public Comment findById(String id) {
    Optional<Comment> comment = commentRepository.findById(id);
    return comment.orElse(null);
  }

  @Override
  public Comment postComment(CommentReq commentReq) {
    String userId = Utils.getIdCurrentUser();
    boolean check =
        userRepository.existsById(userId) && postRepository.existsById(commentReq.getPostId());
    UserComment userComment = new UserComment();
    if (check) {
      Comment comment = new Comment();
      comment.setRate(0.0);
      comment.setContent(commentReq.getContent());
      comment.setEnabled(true);
      User user = userService.findById(userId);
      Post post = postRepository.findById(commentReq.getPostId()).orElse(null);
      comment.setPost(post.getId());
      comment.setCreateTime(new Date());
      if (commentReq.getPageId() != null) {
        Page p = pageRepository.getById(commentReq.getPageId());
        comment.setPage(p);
      }
      userComment.setFirstName(user.getFirstName());
      userComment.setLastName(user.getLastName());
      userComment.setUserId(user.getId());
      if(user.getImage()!=null)
      {
        userComment.setAvatar(user.getImage().getImgLink());
      }
      userCommentRepository.save(userComment);
      comment.setUserComment(userComment);
      commentRepository.save(comment);
      List<Comment> comments = new ArrayList<>();
      if (post != null && post.getComments() != null) {
        comments = post.getComments();
      }
      comments.add(comment);
      post.setComments(comments);
      postRepository.save(post);
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
    if (check) {
      Comment commentUpdate = findById(commentReq.getId());
      List<UserComment> uc = userCommentRepository.findAllByUserId(userId);
      User ucur = userRepository.findUserById(userId);
      UserComment u = uc.get(0);
      if (commentUpdate != null) {
        commentUpdate.setContent(commentReq.getContent());
        commentUpdate.setUserComment(u);
        commentUpdate.setRate(commentReq.getRate());
        if(ucur.getImage()!=null)
        {
          u.setAvatar(ucur.getImage().getImgLink());
        }
        if (commentReq.getPageId() != null) {
          Page p = pageRepository.getById(commentReq.getPageId());
          commentUpdate.setPage(p);
        }
        commentRepository.save(commentUpdate);
        Post p = postRepository.getById(commentReq.getPostId());
        List<Comment> commentList = new ArrayList<>();
        if(p.getComments()!=null)
          commentList = p.getComments();
        for (Comment c: commentList) {
          if(c.getId().equals(commentUpdate.getId()))
          {
            commentList.remove(c);
            commentList.add(commentUpdate);
            break;
          }
        }
        p.setComments(commentList);
        postRepository.save(p);
        return commentUpdate;
      } else {
        throw new AppException(404, "Comment ID not found");
      }
    } else {
      throw new AppException(500, "User not found");
    }

  }

  @Override
  public boolean deleteComment(String id) {
    Comment comment = commentRepository.findById(id).orElse(null);

    if (comment != null) {
      Post p = postRepository.getById(comment.getPost());
      List<Comment> commentList = new ArrayList<>();
      if(p!=null){
        commentList = p.getComments();
        for (Comment c: commentList) {
          if(c.getId().equals(comment.getId()))
          {
            commentList.remove(c);
            break;
          }
        }
        p.setComments(commentList);
        postRepository.save(p);
      }
      commentRepository.delete(comment);
      return true;
    } else {
      throw new AppException(404, "Comment ID not found");
    }

  }



  @Override
  public boolean disabledComment(CommentReq commentReq) {
    Comment cm = commentRepository.getById(commentReq.getId());
    if(cm!=null){
      cm.setEnabled(false);
      return true;
    }
    else{
      return false;
    }
  }



}
