package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public interface ImageService {
    /*
    * The String is image's absolute path
    * */
    String save(MultipartFile multipartFile);
    @ResponseBody byte[] image(String path);
}
