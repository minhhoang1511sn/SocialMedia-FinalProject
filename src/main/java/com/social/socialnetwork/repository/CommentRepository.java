package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> getCommentByPost(Post post);

    List<Comment> findAllByParentComment(String commentid);
    @Query("{'enabled' : false}")
    List<Comment> findAllCommentByEnabled();
    void deleteSubCommentById(String subCommentId);
    boolean existsSubCommentById(String subCommentId);


    void deleteSubListCommentById(String subCommentId);

    Comment getById(String commentParentId);

  List<Comment> getAllCommentByUserComment(String userId);
}
