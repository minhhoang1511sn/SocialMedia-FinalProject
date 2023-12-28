package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.model.User;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PageService {
    Page createPage(PageReq pageReq,  MultipartFile images,MultipartFile background) throws IOException;
    Page findCurrentPage();
    Page updatePage(PageReq pageReq,  MultipartFile images,MultipartFile background) throws IOException;
    Page getById(String id);
    List<Page> getAllPage();
    List<Page> getAllPageLiked();
    boolean deletePage(String id);
    boolean enabledPage(PageReq pageReq);
    boolean isFollowed(String pageId);
    String upAvartar(MultipartFile file)  throws IOException;

    String upBackGround(MultipartFile file)  throws IOException ;

    Page followPage(PageReq pageReq);
}
