package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Story;
import com.social.socialnetwork.model.StoryStored;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoryStoredRepository extends MongoRepository<StoryStored,String> {

}
