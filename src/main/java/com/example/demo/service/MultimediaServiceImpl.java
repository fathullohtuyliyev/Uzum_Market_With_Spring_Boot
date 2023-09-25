package com.example.demo.service;

import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    public static final Path root = Path.of(System.getProperty("user.desktop")+"/files");
    static {
        if (!root.toFile().exists()) {
            System.out.println("root.toFile().mkdirs() = " + root.toFile().mkdirs());
        }
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
            extension = ".".concat(extension);
            String generatedName = "/" + UUID.randomUUID() + extension;
            Path path = Path.of(root + generatedName);
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
            return filename+"."+extension;
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    private static void check(String filename){
        if (filename==null || filename.isBlank()) {
            throw new BadParamException();
        }
    }

    @Override
    public @ResponseBody byte[] image(String filename) {
        try {
            Path path = Path.of(root  + "/" + filename);
            check(filename);
            if (!FilenameUtils.getExtension(path.toString()).equals("jpg")) {
                throw new BadParamException();
            }
            try {
                return Files.readAllBytes(path);
            }catch (FileNotFoundException e){
                throw new NotFoundException();
            }
//            InputStream in = new FileInputStream(path.toFile());
//            return IOUtils.toByteArray(in);
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseEntity<Resource> video(String filename) {
        try {
            check(filename);
            Path path = Path.of(root + "/" + filename);
            if (FilenameUtils.getExtension(path.toString()).equals("mp4")) {
                throw new BadParamException();
            }
            Resource resource = new UrlResource(path.toUri());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
        }catch (MalformedURLException e){
            throw new NotFoundException();
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path path = Path.of(root + "/" + filename);
            Files.deleteIfExists(path);
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
