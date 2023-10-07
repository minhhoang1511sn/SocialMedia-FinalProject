package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Notification;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String> {
    @Query("{'id' : ?0}")
    Notification findNotificationById(String id);
    List<Notification> getNotificationsByUser(User user);
}
