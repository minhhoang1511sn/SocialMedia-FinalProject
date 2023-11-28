package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.model.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PageService {
    Page createPage(PageReq pageReq,  MultipartFile images);
    boolean enabledPage(PageReq pageReq);
}
