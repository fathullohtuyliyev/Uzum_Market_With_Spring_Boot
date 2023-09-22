package com.example.demo.service;

import com.example.demo.exception.BadParamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultimediaServiceImpl implements MultimediaService {
    private static Path root = Path.of(System.getProperty("user.desktop")+"/files");
    static {
        if (!root.toFile().exists()) {
            System.out.println("root.toFile().mkdirs() = " + root.toFile().mkdirs());
        }
        root=Path.of(root+"/");
    }
    @Override
    public String save(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String filename = multipartFile.getOriginalFilename();
            String extension = FilenameUtils.getExtension(filename);
            long byteSize = multipartFile.getSize();
            long kilobyte = byteSize / 1024;
            long megabyte = kilobyte / 1024;
            if (extension==null) {
                throw new BadParamException();
            }
            switch (extension) {
                case "jpg" ->{
                    if (megabyte>1)
                        throw new BadParamException();
                }
                case "mp4" -> {
                    if (megabyte>20)
                        throw new BadParamException();
                }
                default -> throw new BadParamException();
            }
            Path path = Path.of(root + UUID.randomUUID().toString() + "." + extension);
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
            return filename+"."+extension;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public @ResponseBody byte[] image(String filename) {
        try {
            return Files.readAllBytes(Path.of(root+filename));
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseEntity<Resource> video(String filename) {
        Path path = Path.of(root + filename);
        try {
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }catch (MalformedURLException e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
