package com.social.socialnetwork.repository;


import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends MongoRepository<Page,String> {
    @Query("{ 'pageName': { $regex: ?0, $options: 'i' } }")
    List<Page> findByPageByPageName(String query);
    @Query("{'admin' : ?0}")
    Page findByPageByAdmin(User admin);
    Page getById(String pageId);
}
