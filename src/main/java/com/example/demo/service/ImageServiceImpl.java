package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final Path root = Path.of(System.getProperty("user.desktop")+"/files/");
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
            Path path = Path.of(root + UUID.randomUUID().toString() + "." + extension);
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public @ResponseBody byte[] image(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
