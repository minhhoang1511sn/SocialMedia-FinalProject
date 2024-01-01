package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report,String> {

  @Query("{$and: [{ 'comment': null }, { 'page': null }]}")
  List<Report> getAllReportByPost();

  @Query("{$and: [{ 'post': null }, { 'page': null }]}")
  List<Report> getAllReportByComment();
  List<Report> getAllReportByPage();

}
