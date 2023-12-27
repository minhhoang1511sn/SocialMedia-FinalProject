package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.PageService;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.model.Page;
import com.social.socialnetwork.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/cur-page")
    public ResponseEntity<?> getPage(){
        Page page = pageService.findCurrentPage();
        if(page!=null)
        return ResponseEntity.ok(new ResponseDTO(true,"Success",page));
        else
        {
            return ResponseEntity.ok(new ResponseDTO(true,"you don't have page",null));
        }
    }
    @PostMapping(value = "/page", consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> createPage(@ModelAttribute PageReq pageReq
        , @RequestParam(value = "avatar", required =false) MultipartFile avatar
        , @RequestParam(value = "background", required =false) MultipartFile background)
            throws IOException {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    pageService.createPage(pageReq,avatar,background)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }

    @PutMapping(value = "/update-page", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updatePage(@ModelAttribute PageReq pageReq
        , @RequestParam(value = "avatar", required =false) MultipartFile avatar
        , @RequestParam(value = "background", required =false) MultipartFile background)
        throws IOException {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                pageService.updatePage(pageReq,avatar,background)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }
    }
    @DeleteMapping("/delete-page/{id}")
    public ResponseEntity<?> deletePage( @PathVariable String id)
    {
        if (pageService.deletePage(id)) {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ResponseDTO(false, "SubComment ID not exits", null));

    }
    @PutMapping("/enabled-page")
    public ResponseEntity<?> EnablePage(@RequestBody PageReq pageReq){
        boolean check = pageService.enabledPage(pageReq);
        if(check)
            return  ResponseEntity.ok().body(new ResponseDTO(true,"Page has been enabled success",
                    null));
        else
            return  ResponseEntity.ok().body(new ResponseDTO(false,"Page cannot enabled",
                    null));
    }
    @PutMapping("/follow-page")
    public ResponseEntity<?> followPage(@RequestBody PageReq pageReq){
        Page p = pageService.followPage(pageReq);
        if(p!=null)
            return  ResponseEntity.ok().body(new ResponseDTO(true,"User has been follow success",
                null));
        else
            return  ResponseEntity.ok().body(new ResponseDTO(false,"User cannot followed",
                null));
    }
}
