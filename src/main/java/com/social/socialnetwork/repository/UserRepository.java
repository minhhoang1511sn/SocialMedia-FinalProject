package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
@Repository
public interface  UserRepository  extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    User findUserByEmail(String email);

    User findByPhone(String phone);
    @Query("{$and: [{ 'firstName': { $regex: ?0, $options: 'i' } }, { 'lastName': { $regex: ?0, $options: 'i' } }]}")
    List<User> findByFirstNameAndLastName(String query);

    User findUserById(String id);
    Optional<User> findUserByPhone(String phone);
    boolean existsByPhone(String phone);

    @Query("{'isActive' : { $lt: true }}")
    List<User> findAllByStatus(boolean b);
}
