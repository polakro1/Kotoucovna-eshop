package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Image;
import cz.example.kotoucovnaeshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class ImageRepositoryImpl {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/images/products";

    public String upload(MultipartFile image) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, image.getOriginalFilename());
        Files.write(fileNameAndPath, image.getBytes());
        return "/images/products/" + image.getOriginalFilename();
    }

    public void delete(Image image) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, image.getName() + "." + image.getExtension());
        Files.deleteIfExists(fileNameAndPath);
    }

    public String change(MultipartFile newImage, Image oldImage) throws IOException {
        //Nejdříve ověření, zda obrázek nepoužívá jiný produkt
        Long count = jdbcTemplate.queryForObject(
                "select count(*) as count from obrazky where cesta = ?",
                (rs, rowNum) -> {
                    return rs.getLong("count");
                },
                oldImage.getPath()
        );

        if (count == 1) {
            delete(oldImage);
        }

        return upload(newImage);
    }
}
