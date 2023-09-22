package com.example.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MultimediaService {
    /*
    * The String is image's absolute path
    * */
    String save(MultipartFile multipartFile);
    @ResponseBody byte[] image(String path);
    ResponseEntity<Resource> video(String path);
}
