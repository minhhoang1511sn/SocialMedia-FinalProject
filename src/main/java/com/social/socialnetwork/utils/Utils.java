package com.social.socialnetwork.utils;

import com.social.socialnetwork.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
public class Utils {
    private static String pathMedia = "Media";

    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        if (!Files.isDirectory(Path.of(pathMedia))){
            File newDir = new File(pathMedia);
            newDir.mkdir();
        }
        File convFile = new File(pathMedia+"/" +file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static String getIdCurrentUser(){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User u = (User) authentication.getPrincipal();
        return u.getId();
    }

}
