package com.example.demo.service;

import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultimediaServiceImpl implements MultimediaService {
    public static final Path root = Path.of(System.getProperty("user.home")+"/Desktop/files");
//    private final Path path = Path.of(System.getProperty("user.home")+"/Desktop/files");
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
            String generatedName = UUID.randomUUID()  + "." + extension;
            Path path = Path.of(root + "/" + generatedName);
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
            return generatedName;
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
            if (path.toFile().exists()) {
                return Files.readAllBytes(path);
            }
            check(filename);
            if (!FilenameUtils.getExtension(path.toString()).equals("jpg")) {
                throw new BadParamException();
            }
            return Files.readAllBytes(path);
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
    public File video(String filename) {
        try {
            check(filename);
            Path path = Paths.get(root + "/" + filename);
            if (path.toFile().exists()) {
                return path.toFile();
            }
            if (FilenameUtils.getExtension(path.toString()).equals("mp4")) {
                throw new BadParamException();
            }
            return path.toFile();
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean isExist(String filename) {
        return new File(root+"/"+filename).exists();
    }

    @Override
    public void video2(HttpServletResponse response, String filename) {
        try {
            // Get the video file from the filesystem.
            File videoFile = new File(root+"/"+filename);
            if (!videoFile.exists()) {
                throw new NotFoundException();
            }
            // Set the content type of the response.
            response.setContentType("video/mp4");

            // Set the content length of the response.
            response.setContentLength((int) videoFile.length());

            // Stream the video file to the response.
            Files.copy(videoFile.toPath(), response.getOutputStream());
        }catch (IllegalArgumentException e){
            throw new BadParamException();
        }catch (BadParamException | NotFoundException | ForbiddenAccessException e){
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
