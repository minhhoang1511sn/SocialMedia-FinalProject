package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.FriendService;
import com.social.socialnetwork.Service.NotificationService;
import com.social.socialnetwork.Service.PostService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.NotificationDTO;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.*;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PostServiceIplm implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CloudinaryUpload cloudinaryUpload;
  private final ImageRepository imageRepository;
  private final VideoRepository videoRepository;
  private final CommentRepository commentRepository;
  private final UserPostRepository userPostRepository;
  private final UserService userService;
  private final FriendService friendService;
  private final PageRepository pageRepository;
  private final PostLikeRepository postLikeRepository;
  private final PagePostRepository pagePostRepository;
  private final NotificationService notificationService;

  @Override
  public Post createPost(PostReq postReq, List<MultipartFile> images, List<MultipartFile> video, List<String> tagsId) {
    String idCurrentUser = Utils.getIdCurrentUser();
    boolean check = userRepository.existsById(idCurrentUser);

    if (check) {
      Post post = new Post();
      User user = userService.findById(idCurrentUser);

      UserPost userPost = new UserPost();
      userPost.setUserId(user.getId());
        if (user.getImage() != null) {
            userPost.setAvatar(user.getImage().getImgLink());
        }
      userPost.setFirstName(user.getFirstName());
      userPost.setLastName(user.getLastName());
      userPostRepository.save(userPost);

      post.setContent(postReq.getContent());
      post.setCountLike(0L);
      post.setPostType(PostType.PUBLIC);
      post.setUserPost(userPost);
      post.setEnabled(true);
      post.setFeeling(postReq.getFeeling());
      Date now = new Date();
      post.setCreateDate(new Date(now.getTime()));

      //khi pageid c thì đăng voi tư các page
      if (postReq.getPage() != null) {
        Page p = pageRepository.getById(postReq.getPage());
        PagePost pp = getPagePost(p);
        post.setPagePost(pp);
      }

      postRepository.save(post);
      //get list posst của page
      if (postReq.getPage() != null) {
        Page p = pageRepository.getById(postReq.getPage());
        List<Post> postPage = new ArrayList<>();
        if(p!=null)
        {
          if(p.getPosts()!=null)
          {
           postPage = p.getPosts();
          }
          postPage.add(post);
          p.setPosts(postPage);
          pageRepository.save(p);
          user.setPage(p);
        }

        userRepository.save(user);
      }

      List<Image> listImg = new ArrayList<>();
      if (images != null) {
        for (MultipartFile image : images) {
          Image img = uploadImage(post.getId(), image);
          listImg.add(img);
        }
        post.setImages(listImg);
        List<Image> userImg = new ArrayList<>();
        if(user.getImages()!=null)
          userImg = user.getImages();
        userImg.addAll(listImg);
      }
      List<Video> videoList = new ArrayList<>();
      if (video != null) {
        for (MultipartFile v : video) {
          Video vd = uploadVideo(post.getId(), v);
          videoList.add(vd);
        }
        post.setVideos(videoList);
        List<Video> uservideo = new ArrayList<>();
        if(user.getVideos()!=null)
          uservideo = user.getVideos();
        uservideo.addAll(videoList);
        user.setVideos(uservideo);
      }
      List<User> userTags = new ArrayList<>();
      if (tagsId!=null)
      {
        for (String t: tagsId
        ) {
          User u = userRepository.findUserById(t);
          userTags.add(u);
          post.setUsertags(userTags);
        }

      }
      else {
        post.setUsertags(null);
      }
      postRepository.save(post);
      List<Post> userPosts =new ArrayList<>();
      if (user.getPosts() != null) {
        userPosts = user.getPosts();
      }
      userPosts.add(post);
      user.setPosts(userPosts);
      userRepository.save(user);
      return post;
    } else {
      throw new AppException(404, "Post not exits.");
    }
  }

  private PagePost getPagePost(Page p) {
    PagePost pp = new PagePost();
    if(p !=null ) {
      if(p.getAvatar()!=null)
          pp.setAvatar(p.getAvatar());
      pp.setPageId(p.getId());
      pp.setPageName(p.getPageName());
      pp.setAdmin(p.getAdmin());
      pp.setCategory(p.getCategory());
      pp.setContact(p.getContact());
      pp.setBackground(p.getBackground());
      pp.setCountMember(p.getCountMember());
      pp.setEnabled(p.isEnabled());
      pp.setIntroduce(p.getIntroduce());
      pp.setCreateTime(p.getCreateTime());
      pp.setPosts(p.getPosts());
      pp.setImages(p.getImages());
      pp.setVideos(p.getVideos());
      pagePostRepository.save(pp);
    }
    return pp;
  }

  @Override
  public Post findById(String id) {
    Optional<Post> post = postRepository.findById(id);
    return post.orElse(null);

  }

  @Override
  public boolean deletePost(String id) {
    Post postDelete = findById(id);
      if (postDelete != null) {
          List<Image> images = postDelete.getImages();
        List<Video> video = postDelete.getVideos();
          List<Comment> commentList = postDelete.getComments();

          if (images != null) {
              for (Image image : images) {
                  imageRepository.deleteById(image.getId());
              }
          }
        if (video != null) {
          for (Video v : video) {
            videoRepository.deleteById(v.getId());
          }
        }

          if (commentList != null) {
              for (Comment comment : commentList) {
                  comment.setUserComment(null);
                  comment.setPost(null);
                  commentRepository.deleteById(comment.getId());
              }
          }

          postDelete.setComments(null);
          postDelete.setImages(null);
        postDelete.setVideos(null);
        postDelete.setUsertags(null);
        postDelete.setUserPost(null);
          postDelete.setPostType(null);
          postRepository.deleteById(id);
          return true;
      } else {
          throw new AppException(404, "Post does not exits.");
      }
  }

  @Override
  public List<Post> getAllPost(String id) {
    User user = userRepository.findUserById(id);
    List<Post> posts = user.getPosts();
    return posts;
  }

  @Override
  public List<Post> getAllPostByPageId(String id) {
    Page p = pageRepository.getById(id);
    if(p!=null){
      return p.getPosts();
    }
    return null;
  }
  @Override
  public List<Post> getAllPostByUserNotPage(String id) {
    User p = userRepository.findUserById(id);
    List<Post> postByUserPage = p.getPosts();
    List<Post> postByUserNotPage = new ArrayList<>();
    for (Post pt :postByUserPage) {
      if(pt.getPagePost()==null)
      {
        postByUserNotPage.add(pt);
      }
    }
    return postByUserNotPage;
  }
  @Override
  public Post updatePost(PostReq postReq, List<MultipartFile> images, List<MultipartFile> video, List<String> tagsId) {
    Post postUpdate = postRepository.findById(postReq.getId()).orElse(null);
    String idCurrentUser = Utils.getIdCurrentUser();
    User u = userRepository.findUserById(idCurrentUser);
    if (postUpdate != null && postUpdate.getUserPost().getUserId().equals(idCurrentUser)) {
      List<Image> listImg = new ArrayList<>();

      if (images != null) {
        for (MultipartFile image : images) {
          Image img = uploadImage(postUpdate.getId(), image);
          listImg.add(img);
        }
        postUpdate.setImages(listImg);
        List<Image> userImg = new ArrayList<>();
        if(u.getImages()!=null)
          userImg = u.getImages();
        userImg.addAll(listImg);
        u.setImages(userImg);
      }
      List<Video> videoList = new ArrayList<>();
      if (video != null) {
        for (MultipartFile v : video) {
          Video vd = uploadVideo(postUpdate.getId(), v);
          videoList.add(vd);
        }
        postUpdate.setVideos(videoList);
        List<Video> uservideo = new ArrayList<>();
        if(u.getVideos()!=null)
          uservideo = u.getVideos();
        uservideo.addAll(videoList);
        u.setVideos(uservideo);
      }
      postUpdate.setVideos(videoList);
        List<Comment> commentList = postUpdate.getComments();
        List<UserPost> uc = userPostRepository.findAllByUserId(idCurrentUser);
        UserPost up = uc.get(0);
        PostType postType = postUpdate.getPostType();
        postUpdate.setContent(postReq.getContent());
        postUpdate.setUserPost(up);
        postUpdate.setFeeling(postReq.getFeeling());
        postUpdate.setComments(commentList);
        if (postReq.getPage() != null) {
          postUpdate.setPagePost(pagePostRepository.getById(postReq.getPage()));
        }
        Date now = new Date();
        postUpdate.setCreateDate(new Date(
            now.getTime() - (postReq.getCreateDate() != null ? postReq.getCreateDate().getTime()
                : 0)));
        postUpdate.setPostType(postType);
        postRepository.save(postUpdate);
      if (postReq.getPage() != null) {
        Page p = pageRepository.getById(postReq.getPage());
        List<Post> postPage = new ArrayList<>();
        if(p.getPosts()!=null)
          postPage = p.getPosts();
        for (Post po : postPage) {
          if(po.getId().equals(postUpdate.getId()))
          {
            postPage.remove(po);
            postPage.add(postUpdate);
          }
        }
        postPage.add(postUpdate);
        p.setPosts(postPage);
        pageRepository.save(p);

      }
      userRepository.save(u);
      return postUpdate;
    } else {
        throw new AppException(400, "Post does not exists");
      }
  }

  @Override
  public Image uploadImage(String postId, MultipartFile images) {
    String idCurrentUser = Utils.getIdCurrentUser();
    boolean check = userRepository.existsById(idCurrentUser);
    User user = userRepository.findUserById(idCurrentUser);

    Image image = new Image();
    Post post = postRepository.getById(postId);

      if (check && post != null) {
          try {
              String url = cloudinaryUpload.uploadImage(images, null);
              image.setImgLink(url);
              image.setPostType(post.getPostType());
          } catch (IOException e) {
              throw new AppException(400, "Failed");
          };

          imageRepository.save(image);

//          List<Image> imageList = imageRepository.getAllImageByPost(post);
//          if (imageList == null || (long) imageList.size() == 0) {
//              imageList = new ArrayList<>();
//          }
//          imageList.add(image);
//          post.setImages(imageList);
//
//          List<Image> imageUser = new ArrayList<>();
//          if(user.getImages()!=null){
//            imageUser = user.getImages();
//          }
//
//          if (post.getPagePost() != null) {
//            PagePost p = post.getPagePost();
//            List<Image> imagePage = new ArrayList<>();
//            if(p.getImages()!=null)
//            {
//              imagePage = p.getImages();
//            }
//              imagePage.add(image);
//              p.setImages(imagePage);
//          } else {
//              imageUser.add(image);
//              user.setImages(imageUser);
//          }

//          postRepository.save(post);
//          List<Post> postU = new ArrayList<>();
//          if(user.getPosts()!=null)
//            postU = user.getPosts();
//            postU.add(post);
//            user.setPosts(postU);
//            userRepository.save(user);
          return image;
      } else {
          return null;
      }

  }

  @Override
  public Video uploadVideo(String postId, MultipartFile videos) {
    String idCurrentUser = Utils.getIdCurrentUser();
    boolean check = userRepository.existsById(idCurrentUser);
    User user = userRepository.findUserById(idCurrentUser);
    Video video = new Video();
    Post post = postRepository.getById(postId);
      if (check && post != null) {
          try {
              String url = cloudinaryUpload.uploadVideo(videos, null);
              video.setVideoLink(url);
              video.setPostType(post.getPostType());
          } catch (IOException e) {
              throw new AppException(400, "Failed");
          }
          ;
          videoRepository.save(video);
//          List<Video> videoList = videoRepository.getAllVideoByPost(post);
//          if (videoList == null || (long) videoList.size() == 0) {
//              videoList = new ArrayList<>();
//          }
//          videoList.add(video);
//          post.setVideos(videoList);
//
//          if (post.getPagePost() != null) {
//            PagePost p = post.getPagePost();
//            List<Video> videoPage = new ArrayList<>();
//            if(p.getVideos()!=null)
//            {
//               videoPage = p.getVideos();
//            }
//
//
//              if (videoPage == null) {
//                  videoPage = new ArrayList<>();
//              }
//              videoPage.add(video);
//              p.setVideos(videoPage);
//              pagePostRepository.save(p);
//          } else {
//              List<Video> videoUser = user.getVideos();
//              if (videoUser == null) {
//                  videoUser = new ArrayList<>();
//              }
//              videoUser.add(video);
//              user.setVideos(videoUser);
//          }
//
//          postRepository.save(post);
//          userRepository.save(user);
          return video;
      } else {
          return null;
      }
  }

  @Override
  public List<Post> gettingPostByFriend() {
    List<User> users = userRepository.findAll();
    User curU = userService.getCurrentUser();
    List<Page> pageFollow = new ArrayList<>();

    if(curU.getPagefollowed()!=null)
      pageFollow = curU.getPagefollowed();
    List<Post> newfeeds = new ArrayList<>();
    List<String> curUserFriend = new ArrayList<>();
    List<String> friendUserFriend = new ArrayList<>();
    if(curU.getUserFriend()!=null){
      curUserFriend = curU.getUserFriend();
    }

    for (User u: users) {
      if(u.getUserFriend()!=null){
        friendUserFriend = u.getUserFriend();
      }
      if ( friendUserFriend.contains(curU.getId()) && curUserFriend.contains(u.getId())
          && u.getPosts() != null) {
        newfeeds.addAll(u.getPosts());
      }
    }

    for (Page p : pageFollow) {
      if (p.getPosts() != null && curU.getPagefollowed().contains(p)) {
        newfeeds.addAll(p.getPosts());
      }
    }
    if(curU.getPosts()!=null)
    {
      List<Post> postPage = curU.getPosts();
      List<Post> postPageResult = new ArrayList<>();
      for (Post p: postPage ) {
        if(p.getPagePost()==null)
        {
          postPageResult.add(p);
        }
      }
      newfeeds.addAll(postPageResult);
    }
    newfeeds.sort(Comparator.comparing(Post::getCreateDate).reversed());
    List<String> postLike = new ArrayList<>();
    for (Post p: newfeeds) {
      if(postLikeRepository.findByUserIdAndPostId(Utils.getIdCurrentUser(),p.getId())!=null)
      {
        postLike.add(p.getId());
      }
    }
    curU.setPostLike(postLike);
    userRepository.save(curU);
    return newfeeds;
  }

  @Override
  public List<Post> getAllPostByUser(String userId) {
    List<Post> posts = postRepository.getAllPostByUser(userId);
    List<Post> result = new ArrayList<>();
    for (Post p : posts) {
      if(p.getPagePost()==null)
      {
        result.add(p);
      }
    }
    return result;
  }

  @Override
  public Post findPostById(String id) {
    return postRepository.getById(id);
  }

  @Override
  public Post likePost(String id) {
    Post post = postRepository.getById(id);
    PostLike postLike = new PostLike();
    if(post != null && postLikeRepository.findByUserIdAndPostId(Utils.getIdCurrentUser(),id)==null){
      postLike.setPostId(id);
      postLike.setUserId(Utils.getIdCurrentUser());
      postLikeRepository.save(postLike);
      Long numberLike = post.getCountLike();
      post.setCountLike(numberLike+1);
      postRepository.save(post);
      User u = userRepository.findUserById(Utils.getIdCurrentUser());
      List<String> ulike = u.getPostLike() == null ? new ArrayList<>() : u.getPostLike();
      ulike.add(postLike.getPostId());
      u.setPostLike(ulike);
      userRepository.save(u);

      //Notification
      NotificationDTO notificationDTO = new NotificationDTO();
      notificationDTO.setContent(post.getUserPost().getFirstName() + " "+post.getUserPost().getLastName() +"is like your post");
      notificationDTO.setCreateTime(new Date());
      notificationDTO.setTypeNotifications(TypeNotifications.POSTNOTIFICATIONS);
      User userP = userRepository.findUserById(post.getUserPost().getUserId());
      notificationDTO.setUser(userP);
      notificationService.newNotificaition(notificationDTO);
    }
    return post;
  }

  @Override
  public Post unlikePost(String id) {
    Post post = postRepository.getById(id);

    if(post != null && postLikeRepository.findByUserIdAndPostId(Utils.getIdCurrentUser(),id)!=null){
      PostLike postLike = postLikeRepository.findByUserIdAndPostId(Utils.getIdCurrentUser(),id);
      User u = userRepository.findUserById(Utils.getIdCurrentUser());
      List<String> ulike = u.getPostLike() == null ? new ArrayList<>() : u.getPostLike();
      ulike.remove(postLike.getPostId());
      u.setPostLike(ulike);
      userRepository.save(u);
      postLikeRepository.delete(postLike);
      Long numberLike = post.getCountLike();
      post.setCountLike(numberLike-1);
      postRepository.save(post);
    }
    return post;
  }


}
