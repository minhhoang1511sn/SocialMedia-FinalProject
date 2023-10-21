package com.social.socialnetwork.Service.Iplm;


import com.social.socialnetwork.Service.PageService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.PageRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class PageServiceIplm implements PageService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PageRepository pageRepository;
    @Override
    public Page createPage(PageReq pageReq,  MultipartFile images) {
        String idCurrentUser = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(idCurrentUser);
        if(check){
            Page page = new Page();
            User user = userService.findById(idCurrentUser);
            page.setPageName(page.getPageName());
            page.setAdmin(user);
            page.setCreateTime(new Date());
            page.setIntroduce(page.getIntroduce());
            page.setCountMember((long) 0);
            page.setImages(null);
            page.setPageName(null);
            page.setVideos(null);
            if(pageReq.getAvatar()!=null)
                page.setAvatar(page.getAvatar());

            pageRepository.save(page);
            user.setPage(page);
            userRepository.save(user);
            return page;
        } else {
            throw new AppException(404, "Product or Comment not exits.");
        }

    }
}
