package com.social.socialnetwork.Controller;


import com.social.socialnetwork.Service.ReportService;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.ReportReq;
import com.social.socialnetwork.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class ReportController {
    private final ReportService reportService;
    @PostMapping("/report")
    public ResponseEntity<?> createReport(@RequestBody ReportReq reportReq) {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success", reportService.createReport(reportReq)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(), null));
        }

    }
}
