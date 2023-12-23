package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.model.PostLike;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostLikeRepository extends MongoRepository<PostLike,String> {
  PostLike findByUserIdAndPostId(String userId, String postId);
}
