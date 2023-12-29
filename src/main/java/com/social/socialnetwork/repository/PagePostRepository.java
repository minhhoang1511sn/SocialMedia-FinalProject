package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Notification;
import com.social.socialnetwork.model.PagePost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagePostRepository extends MongoRepository<PagePost,String> {

}
