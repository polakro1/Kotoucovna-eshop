package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.repository.ProductRepository;
import cz.example.kotoucovnaeshop.repository.impl.ImageRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ImageRepositoryImpl imageRepository;

    public List<Product> getAllProductsInCategory(Category category) {
        return repository.findAllByCategory(category);
    }

    public Product getProductById(long productId) {
        return repository.getProduct(productId);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public void update(Product product) {
        repository.update(product);
    }

    public void save(Product product) {
        repository.save(product);
    }

    public void save(Product product, MultipartFile image) throws IOException {
        product.setImageUrl(imageRepository.upload(image));
        repository.save(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public void uploadImage(MultipartFile image) throws IOException {
        imageRepository.upload(image);
    }
}
