package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.FriendService;
import com.social.socialnetwork.Service.PostService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.*;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


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
      Date now = new Date();
      post.setCreateDate(new Date(now.getTime()));
      if (postReq.getPageId() != null) {
        Page p = pageRepository.getById(postReq.getPageId());
        post.setPage(p);
      }
      postRepository.save(post);

      List<Post> userPosts = user.getPosts();
      if (userPosts == null) {
        userPosts = new ArrayList<>();
      }
      userPosts.add(post);
      user.setPosts(userPosts);
      userRepository.save(user);
      List<Image> listImg = new ArrayList<>();
      if (images != null) {
        for (MultipartFile image : images) {
          Image img = uploadImage(post.getId(), image);
          listImg.add(img);
        }
        post.setImages(listImg);
      }
      List<Video> videoList = new ArrayList<>();
      if (video != null) {
        for (MultipartFile v : video) {
          Video vd = uploadVideo(post.getId(), v);
          videoList.add(vd);
        }
        post.setVideos(videoList);
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
      return post;
    } else {
      throw new AppException(404, "Product or Comment not exits.");
    }
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
          List<Comment> commentList = postDelete.getComments();
          if (images != null) {
              for (Image image : images) {
                  imageRepository.deleteById(image.getId());
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
  public Post updatePost(PostReq postReq, MultipartFile images) {
    Post postUpdate = postRepository.findById(postReq.getId()).orElse(null);
    String idCurrentUser = Utils.getIdCurrentUser();
      if (postUpdate != null) {
          if (images != null) {
              uploadImage(postUpdate.getId(), images);
          }
          List<Comment> commentList = postUpdate.getComments();
          List<UserPost> uc = userPostRepository.findAllByUserId(idCurrentUser);
          UserPost u = uc.get(0);
          PostType postType = postUpdate.getPostType();
          postUpdate.setContent(postReq.getContent());
          postUpdate.setUserPost(u);
          postUpdate.setComments(commentList);
          if (postReq.getPageId() != null) {
              Page p = pageRepository.getById(postReq.getPageId());
              postUpdate.setPage(p);
          }
          Date now = new Date();
          postUpdate.setCreateDate(new Date(
              now.getTime() - (postReq.getCreateDate() != null ? postReq.getCreateDate().getTime()
                  : 0)));
          postUpdate.setPostType(postType);
          return postRepository.save(postUpdate);
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
          }
          ;
          imageRepository.save(image);
          List<Image> imageList = imageRepository.getAllImageByPost(post);
          if (imageList == null || (long) imageList.size() == 0) {
              imageList = new ArrayList<>();
          }
          imageList.add(image);
          post.setImages(imageList);
          List<Image> imageUser = user.getImages();

          if (post.getPage() != null) {
              List<Image> imagePage = user.getImages();
              Page p = post.getPage();
              if (imagePage == null) {
                  imagePage = new ArrayList<>();
              }
              imagePage.add(image);
              p.setImages(imagePage);
          } else {
              if (imageUser == null) {
                  imageUser = new ArrayList<>();
              }
              imageUser.add(image);
              user.setImages(imageUser);
          }

          postRepository.save(post);
          userRepository.save(user);
          //String url =image.getImgLink();
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
          List<Video> videoList = videoRepository.getAllVideoByPost(post);
          if (videoList == null || (long) videoList.size() == 0) {
              videoList = new ArrayList<>();
          }
          videoList.add(video);
          post.setVideos(videoList);

          if (post.getPage() != null) {
              List<Video> videoPage = user.getVideos();
              Page p = post.getPage();
              if (videoPage == null) {
                  videoPage = new ArrayList<>();
              }
              videoPage.add(video);
              p.setVideos(videoPage);
          } else {
              List<Video> videoUser = user.getVideos();
              if (videoUser == null) {
                  videoUser = new ArrayList<>();
              }
              videoUser.add(video);
              user.setVideos(videoUser);
          }

          postRepository.save(post);
          userRepository.save(user);
          return video;
      } else {
          return null;
      }
  }

  @Override
  public List<Post> gettingPostByFriend() {
    List<User> users = userRepository.findAll();
    User curU = userService.getCurrentUser();
    List<Post> newfeeds = new ArrayList<>();
    users.forEach(u -> {
        if (friendService.isFriend(curU, u)) {
            newfeeds.addAll(u.getPosts());
        }
    });
    newfeeds.addAll(curU.getPosts());
    newfeeds.sort(Comparator.comparing(Post::getCreateDate).reversed());
    return newfeeds;
  }

  @Override
  public List<Post> getAllPostByUser(String userId) {
    List<Post> posts = postRepository.getAllPostByUser(userId);
    return posts;
  }

  @Override
  public Post findPostById(String id) {
    return postRepository.getById(id);
  }


}
