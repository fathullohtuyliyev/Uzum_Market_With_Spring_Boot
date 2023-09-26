package com.example.demo.controller;

import com.example.demo.service.MultimediaService;
import com.example.demo.service.MultimediaServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;


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
