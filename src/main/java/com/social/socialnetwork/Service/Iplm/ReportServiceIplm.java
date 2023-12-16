package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.repository.CommentRepository;
import com.social.socialnetwork.repository.PageRepository;
import com.social.socialnetwork.utils.Utils;
import com.social.socialnetwork.Service.ReportService;
import com.social.socialnetwork.dto.ReportReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.Report;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.PostRepository;
import com.social.socialnetwork.repository.ReportRepository;
import com.social.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceIplm implements ReportService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final PageRepository pageRepository;
    @Override
    public Report createReport(ReportReq reportReq) {
        User user = userRepository.findUserById(Utils.getIdCurrentUser());
        Post post = postRepository.getById(reportReq.getPostId());
        Page page = pageRepository.getById(reportReq.getPageId());
        Comment comment = commentRepository.getById(reportReq.getCommentId());
        if(user!=null && post!=null || user!=null && comment!=null || user!=null && page!=null){
            Report report = new Report();
            report.setContentReport(reportReq.getContentReport());
            report.setUser(user);
            report.setPost(post);
            report.setComment(comment);
            report.setPage(page);
            reportRepository.save(report);
            return report;
        } else {
            throw new AppException(404, "Post or Comment not exits.");
        }
    }
    public List<Report> getAllReport()
    {
        return reportRepository.findAll();
    }


}
