package com.social.socialnetwork.Controller.admin;

import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

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
}
