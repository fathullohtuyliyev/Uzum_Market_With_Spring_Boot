package com.example.demo.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@Service
public interface MultimediaService {
    /*
    * The String is image's absolute path
    * */
    String save(MultipartFile multipartFile);
    @ResponseBody byte[] image(String filename);
    File video(String filename);
    Boolean isExist(String filename);
    void video2(HttpServletResponse response, String filename);
    void delete(String filename);
}
