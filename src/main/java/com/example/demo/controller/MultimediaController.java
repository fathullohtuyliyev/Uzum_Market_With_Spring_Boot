package com.example.demo.controller;

import com.example.demo.exception.NotFoundException;
import com.example.demo.service.MultimediaService;
import com.example.demo.service.MultimediaServiceImpl;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


@RestController
@Slf4j
@RequestMapping("/api.multimedia")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MultimediaController {
    private final MultimediaService multimediaService;
    private final ResourceLoader resourceLoader;

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> save(@RequestParam(value = "file") MultipartFile file){
        String save = multimediaService.save(file);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
//    @GetMapping(value = "video/{title}")
    public Mono<Resource> getVideos(@PathVariable String title) throws MalformedURLException {
        String s = MultimediaServiceImpl.root + "\\" + title;
        if (new File(s).exists()) {
            System.out.println("Exist : "+s);
        }else {
            System.out.println("Exist not:"+s);
        }
        Path path = Path.of(s);
        Resource resource = new UrlResource(path.toUri());
        System.out.println("file = " + s);
        return Mono.fromSupplier(() -> resource);
    }



    @PreAuthorize("permitAll()")
    @GetMapping("/video/{filename}")
    public void downloadVideo(@PathVariable String filename,
                                                  HttpServletResponse response) throws IOException {
        multimediaService.video2(response,filename);
    }



    @PreAuthorize("permitAll()")
    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String filename) throws IOException {
        return multimediaService.image(filename);
    }
    @DeleteMapping("/delete/{filename}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable String filename){
        multimediaService.delete(filename);
        return ResponseEntity.noContent().build();
    }
}
