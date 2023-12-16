package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.PageService;
import com.social.socialnetwork.Service.StoryService;
import com.social.socialnetwork.dto.PageReq;
import com.social.socialnetwork.dto.ResponseDTO;
import com.social.socialnetwork.dto.StoryReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class StoryController {
    private final StoryService storyService;
    @PostMapping(value = "/story", consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> createStory(@ModelAttribute StoryReq storyReq, @RequestParam(value = "image", required =
            false) MultipartFile image)
            throws IOException {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    storyService.createStory(storyReq,image)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }

    }

    @PutMapping( "/story")
    public ResponseEntity<?> disabledStory(@RequestBody StoryReq storyReq)
            throws IOException {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    storyService.disabledStory(storyReq)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }

    }
}
