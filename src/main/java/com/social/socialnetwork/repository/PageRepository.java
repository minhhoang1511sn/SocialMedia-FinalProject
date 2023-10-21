package com.social.socialnetwork.repository;


import com.social.socialnetwork.model.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends MongoRepository<Page,String> {
}
