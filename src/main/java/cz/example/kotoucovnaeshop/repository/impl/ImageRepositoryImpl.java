package cz.example.kotoucovnaeshop.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class ImageRepositoryImpl {
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/images/products";

    public String upload(MultipartFile image) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, image.getOriginalFilename());
        Files.write(fileNameAndPath, image.getBytes());
        return "/images/products/" + image.getOriginalFilename();
    }
}
