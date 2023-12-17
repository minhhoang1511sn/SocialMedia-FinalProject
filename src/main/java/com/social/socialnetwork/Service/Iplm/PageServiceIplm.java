package com.social.socialnetwork.Service.Iplm;


import com.social.socialnetwork.Service.PageService;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.ImageRepository;
import com.social.socialnetwork.repository.PageRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PageServiceIplm implements PageService {

  private final UserRepository userRepository;
  private final PageRepository pageRepository;
  private final ImageRepository imageRepository;

  @Override
  public Page createPage(PageReq pageReq, MultipartFile images) {
    String idCurrentUser = Utils.getIdCurrentUser();
    User user = userRepository.findUserById(idCurrentUser);
    if (user != null && pageRepository.findByPageByAdmin(user) == null) {
      Page page = new Page();
      page.setPageName(pageReq.getPageName());
      page.setAdmin(user.getId());
      page.setCreateTime(new Date());
      page.setIntroduce(pageReq.getIntroduce());
      page.setCountMember((long) 0);
      page.setVideos(null);
      List<Image> pageImgs = new ArrayList<>();
      if (images != null) {
        Image avt = new Image();
        avt.setImgLink(pageReq.getAvatar());
        avt.setPostType(PostType.PUBLIC);
        imageRepository.save(avt);
        page.setAvatar(avt);
        pageImgs.add(avt);
        page.setImages(pageImgs);
      } else {
        page.setImages(null);
      }

      page.setEnabled(true);
      pageRepository.save(page);
      return page;
    } else {
      throw new AppException(404, "You are already admin 1 page");
    }

  }

  @Override
  public boolean enabledPage(PageReq pageReq) {
    if (pageRepository.existsById(pageReq.getId()) && pageReq.getEnabled()) {
      pageReq.setEnabled(false);
    } else {
      pageReq.setEnabled(true);
    }
    return pageReq.getEnabled();
  }

  @Override
  public Page followPage(PageReq pageReq) {
    Page page = pageRepository.getById(pageReq.getId());
    User user = userRepository.findUserById(Utils.getIdCurrentUser());
    if(user.getPagefollowed()==null)
    {
      user.setPagefollowed(new ArrayList<>());
    }
    if(page!=null  && !user.getPagefollowed().contains(page))
    {
      List<Page> pageFollow = user.getPagefollowed();
      pageFollow.add(page);
      user.setPagefollowed(pageFollow);
      userRepository.save(user);
      return page;
    }
    return null;
  }
}
