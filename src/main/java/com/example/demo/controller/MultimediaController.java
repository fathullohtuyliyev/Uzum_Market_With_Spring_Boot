package com.example.demo.controller;

import com.example.demo.service.MultimediaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;


@RestController
@RequestMapping("/api.multimedia")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MultimediaController {
    private final MultimediaService multimediaService;
    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> save(@RequestParam(value = "file") MultipartFile file){
        String save = multimediaService.save(file);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/video/{filename}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String filename) throws MalformedURLException {
        return multimediaService.video(filename);
    }

    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String filename) throws IOException {
        return multimediaService.image(filename);
    }
    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<Void> delete(@PathVariable String filename){
        multimediaService.delete(filename);
        return ResponseEntity.noContent().build();
    }
}
