package com.social.socialnetwork.Service.Iplm;


import com.cloudinary.Cloudinary;
import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.PageService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.ImageRepository;
import com.social.socialnetwork.repository.PageRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import java.io.IOException;
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
  private final UserService userService;
  private final CloudinaryUpload cloudinaryUpload;

  @Override
  public Page createPage(PageReq pageReq, MultipartFile images,MultipartFile background) throws IOException {
    String idCurrentUser = Utils.getIdCurrentUser();
    User user = userRepository.findUserById(idCurrentUser);
    if (user != null && user.getPage()==null) {
      Page page = new Page();
      page.setPageName(pageReq.getPageName());
      page.setAdmin(user.getId());
      page.setCreateTime(new Date());
      page.setIntroduce(pageReq.getIntroduce());
      page.setCountMember((long) 0);
      page.setVideos(null);
      page.setCategory(pageReq.getCategory());
      page.setContact(pageReq.getContact());
      List<Image> pageImgs = new ArrayList<>();
      if (page.getImages() != null) {
        pageImgs = page.getImages();
      }
      if(images!=null){
        Image avt = new Image();
        String img = upAvartar(images);
        avt.setImgLink(img);
        avt.setPostType(PostType.PUBLIC);
        imageRepository.save(avt);
        page.setAvatar(avt);
        pageImgs.add(avt);
        page.setImages(pageImgs);
      } else {
        page.setImages(null);
      }
      if (background != null) {
        Image bg = new Image();
        String img = upAvartar(background);
        bg.setImgLink(img);
        bg.setPostType(PostType.PUBLIC);
        imageRepository.save(bg);
        page.setBackground(bg);
        pageImgs.add(bg);
        page.setImages(pageImgs);
      } else {
        page.setImages(null);
      }
      page.setEnabled(true);
      pageRepository.save(page);
      user.setPage(page);
       List<Page> flPage= new ArrayList<>();
      if(user.getPagefollowed()!=null)
      {
        flPage  = new ArrayList<>(user.getPagefollowed());
      }
      flPage.add(page);
      user.setPagefollowed(flPage);
      userRepository.save(user);
      return page;
    } else {
      throw new AppException(404, "You are already admin 1 page");
    }

  }

  @Override
  public Page findCurrentPage() {
    User curU = userService.findById(Utils.getIdCurrentUser());
    Page p = curU.getPage();
    return p;
  }

  @Override
  public Page updatePage(PageReq pageReq, MultipartFile images,MultipartFile background) throws IOException {
    String idCurrentUser = Utils.getIdCurrentUser();
    User user = userRepository.findUserById(idCurrentUser);
    Page page = user.getPage();
    if (page != null) {
      page.setPageName(pageReq.getPageName());
      page.setIntroduce(pageReq.getIntroduce());
      page.setVideos(null);
      page.setCategory(pageReq.getCategory());
      page.setContact(pageReq.getContact());
      List<Image> pageImgs = new ArrayList<>();

      if(page.getImages()!=null)
      {
        pageImgs = page.getImages();
      }
      if (images != null) {
        Image avt = new Image();
        String img = upAvartar(images);
       avt.setImgLink(img);
        avt.setPostType(PostType.PUBLIC);
        imageRepository.save(avt);
        page.setAvatar(avt);
        pageImgs.add(avt);
        page.setImages(pageImgs);
      }
      if (background != null) {
        Image bg = new Image();
        String img = upAvartar(background);
        bg.setImgLink(img);
        bg.setPostType(PostType.PUBLIC);
        imageRepository.save(bg);
        page.setBackground(bg);
        pageImgs.add(bg);
        page.setImages(pageImgs);
      }
      pageRepository.save(page);
      user.setPage(page);
      userRepository.save(user);
      return page;
    } else {
      throw new AppException(404, "Update Page failed");
    }
  }

  @Override
  public Page getById(String id) {
    return pageRepository.getById(id);
  }

  @Override
  public List<Page> getAllPage() {
    return pageRepository.findAll();
  }

  @Override
  public List<Page> getAllPageLiked() {
    User cur = userRepository.findUserById(Utils.getIdCurrentUser());
    return cur.getPagefollowed();
  }

  @Override
  public boolean deletePage(String id) {
    Page pageDel = pageRepository.findById(id).orElse(null);

    if (pageDel != null) {
      User admin = userRepository.findUserById(pageDel.getAdmin());
        admin.setPage(null);
        userRepository.save(admin);
      pageRepository.delete(pageDel);
      return true;
      }
    {
      throw new AppException(404, "Page ID not found");
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
  public boolean isFollowed(String pageId) {
    User cur = userRepository.findUserById(Utils.getIdCurrentUser());
    Page testPage = pageRepository.getById(pageId);
    List<Page> pageFollowed = new ArrayList<>();
    if(cur.getPagefollowed()!=null)
    {
      pageFollowed = cur.getPagefollowed();
    }
    for (Page test: pageFollowed) {
      if(test.getId().equals(testPage.getId()))
        return true;
    }
    return false;
  }

  @Override
  public String upAvartar(MultipartFile file) throws IOException {
    String id = Utils.getIdCurrentUser();
    User user = userService.findById(id);
    if (user == null)
      throw new AppException(404, "User ID not found");
    Page p = new Page();
    if(user.getPage()!=null)
       p = user.getPage();

    Image imgUrl = new Image();
    if (p.getAvatar() != null && p.getAvatar().getImgLink().startsWith("https://res.cloudinary.com/minhhoang1511/image/upload")) {
      imgUrl = p.getAvatar();
      imgUrl.setImgLink(cloudinaryUpload.uploadImage(file,imgUrl.getImgLink()));
    }else
      imgUrl.setImgLink(cloudinaryUpload.uploadImage(file,null));
    imgUrl.setPostType(PostType.PUBLIC);
    imageRepository.save(imgUrl);

    return imgUrl.getImgLink();
  }

  @Override
  public String upBackGround(MultipartFile file) throws IOException {
    String id = Utils.getIdCurrentUser();
    User user = userService.findById(id);
    if (user == null)
      throw new AppException(404, "User ID not found");
    Page p = new Page();
    if(user.getPage()!=null)
      p = user.getPage();

    Image imgUrl = new Image();
    if (p.getBackground() != null && p.getBackground().getImgLink().startsWith("https://res.cloudinary.com/minhhoang1511/image/upload")) {
      imgUrl = p.getBackground();
      imgUrl.setImgLink(cloudinaryUpload.uploadImage(file,imgUrl.getImgLink()));
    }else
      imgUrl.setImgLink(cloudinaryUpload.uploadImage(file,null));
    imgUrl.setPostType(PostType.PUBLIC);
    imageRepository.save(imgUrl);

    return imgUrl.getImgLink();
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
