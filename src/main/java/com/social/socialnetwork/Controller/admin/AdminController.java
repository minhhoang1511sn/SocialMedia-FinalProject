package com.social.socialnetwork.Controller.admin;

import com.social.socialnetwork.Service.CommentService;
import com.social.socialnetwork.Service.ReportService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final ReportService reportService;
    private final CommentService commentService;

    @PutMapping("/disable-user/{id}")
    public ResponseEntity<?> DisabledUser(@PathVariable String id){
        boolean check = userService.disabledUser(id);
        if(check)
            return  ResponseEntity.ok().body(new ResponseDTO(true,"User has been disabled success",
                    null));
        else
            return  ResponseEntity.ok().body(new ResponseDTO(false,"User cannot disabled",
                    null));
    }
    @PutMapping("/enabled-user/{id}")
    public ResponseEntity<?> EnableUser(@PathVariable String id){
        boolean check = userService.enabledUser(id);
        if(check)
            return  ResponseEntity.ok().body(new ResponseDTO(true,"User has been enabled success",
                    null));
        else
            return  ResponseEntity.ok().body(new ResponseDTO(false,"User cannot enabled",
                    null));
    }
    @GetMapping("/Reports/")
    public ResponseEntity<?> GetAllReport(){

            return  ResponseEntity.ok().body(new ResponseDTO(true,"",
                    reportService.getAllReport()));
    }

    @GetMapping("/page-reported/")
    public ResponseEntity<?> GetAllPageReported(){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            userService.getPageReported()));
    }

    @GetMapping("/post-reported/")
    public ResponseEntity<?> GetAllPostReported(){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            userService.getPostReported()));
    }

    @GetMapping("/comment-reported/")
    public ResponseEntity<?> GetAllCommentReported(){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            userService.getCommentReported()));
    }
    @PutMapping("/disable-comment/")
    public ResponseEntity<?> DisableComment(@RequestBody CommentReq commentReq){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            commentService.disabledComment(commentReq)));
    }
    @PutMapping("/disable-page/")
    public ResponseEntity<?> DisablePage(@RequestBody  PageReq pageReq){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            userService.disabledPage(pageReq)));
    }

    @PutMapping("/disabled-post/")
    public ResponseEntity<?> DisabledPost(@RequestBody PostReq postReq){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            userService.disabledPost(postReq)));
    }

    @DeleteMapping("/delete-page/")
    public ResponseEntity<?> DeletePage(@RequestBody  PageReq pageReq){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            userService.deletePage(pageReq)));
    }

    @DeleteMapping("/delete-post/")
    public ResponseEntity<?> DeletePost(@RequestBody  PostReq postReq){

        return  ResponseEntity.ok().body(new ResponseDTO(true,"",
            userService.deletePost(postReq)));
    }
}
