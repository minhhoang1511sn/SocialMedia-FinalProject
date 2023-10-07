package com.social.socialnetwork.Service.Iplm;

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


@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceIplm implements ReportService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;
    @Override
    public Report createReport(ReportReq reportReq) {
        User user = userRepository.findUserById(Utils.getIdCurrentUser());
        Post post = postRepository.getById(reportReq.getPostId());
        if(user!=null && post!=null){
            Report report = new Report();
            report.setContentReport(reportReq.getContentReport());
            report.setUser(user);
            report.setPost(post);
            reportRepository.save(report);
            return report;
        } else {
            throw new AppException(404, "Post or Comment not exits.");
        }
    }
}
