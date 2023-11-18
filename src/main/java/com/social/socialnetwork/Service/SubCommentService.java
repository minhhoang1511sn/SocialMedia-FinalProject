package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.SubCommentReq;
import com.social.socialnetwork.model.Comment;

import java.util.List;

public interface SubCommentService {
    Comment findById(final String id);

    Comment createSubComment(SubCommentReq subCommentReq);

    Comment updateSubComment(final String id, SubCommentReq subCommentReq);

    List<Comment> findSubCommetByParentCommentId(String commentParentId);
}
