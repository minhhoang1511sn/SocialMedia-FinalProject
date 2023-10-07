package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationCodeRepository extends MongoRepository<ConfirmationCode,String> {

    ConfirmationCode findVerificationCodeByCodeAndUser_Email(String code, String email);
    ConfirmationCode findVerificationCodeByUserEmail(String email);

    ConfirmationCode findVerificationTokenByUserId(String uid);

    ConfirmationCode findConfirmationCodeByCodeAndUserPhone(String code, String formattedPhoneNumber);
}
