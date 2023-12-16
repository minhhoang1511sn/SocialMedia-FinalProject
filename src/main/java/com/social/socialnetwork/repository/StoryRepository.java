package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends MongoRepository<Story,String> {
  Story getStoriesById(String id);
}
