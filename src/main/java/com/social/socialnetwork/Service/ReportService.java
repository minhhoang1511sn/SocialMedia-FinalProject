package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.ReportReq;
import com.social.socialnetwork.model.Report;

public interface ReportService {
    Report createReport(ReportReq reportReq);

}
