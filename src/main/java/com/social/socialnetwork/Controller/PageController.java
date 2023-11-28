package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.PageService;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class PageController {

    private final PageService pageService;
    @PostMapping(value = "/page", consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> createPage(@ModelAttribute PageReq pageReq, @RequestParam(value = "avatar", required =
            false) MultipartFile avatar)
            throws IOException {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    pageService.createPage(pageReq,avatar)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }

    }
    @PutMapping("/enabled-page")
    public ResponseEntity<?> EnablePage(@ModelAttribute PageReq pageReq){
        boolean check = pageService.enabledPage(pageReq);
        if(check)
            return  ResponseEntity.ok().body(new ResponseDTO(true,"User has been enabled success",
                    null));
        else
            return  ResponseEntity.ok().body(new ResponseDTO(false,"User cannot enabled",
                    null));
    }
}
