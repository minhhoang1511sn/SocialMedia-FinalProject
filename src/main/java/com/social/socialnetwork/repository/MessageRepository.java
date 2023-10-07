package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Message;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository  extends MongoRepository<Message,String> {
    @Query("{ $or: [ " +
            "   { 'uSender.userId':'$userId', 'uReceiver.userId':'$companionId'}," +
            "   { 'uSender.userId': '$companionId', 'uReceiver.userId':'$userId'}" +
            "], $orderby: { 'createTime' : 1 } }")
    List<Message> findConversation(String userId, String companionId);
    Message findFirstBySenderIdOrReceiverIdOrderByIdDesc(String id, String id1);
    @Query("[" +
            "{$group: {_id: {uSender: '$uSender', uReceiver: '$uReceiver'}, maxId: {$max: '$id'}}}," +
            "{$match: {$or: [{'_id.uSender.userId':'$id'}, {'_id.uReceiver.userId': '$id'}]}}," +
            "{$project: {maxId: '$_id.maxId'}}," +
            "{$match: {id: {$in: ['$maxId']}}}" +
            "]")
    List<Message> findAllRecentMessages(String id);
}
