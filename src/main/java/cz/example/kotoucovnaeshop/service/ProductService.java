package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Employee;
import cz.example.kotoucovnaeshop.model.Image;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.repository.impl.ImageRepositoryImpl;
import cz.example.kotoucovnaeshop.repository.impl.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    AdminService adminService;

    public List<Product> getAllProductsInCategory(Category category, String orderBy) {
        return repository.findAllByCategory(category, orderBy);
    }

    public Product getProduct(long productId) {
        return repository.getProductById(productId);
    }

    public Product getProduct(String name) {
        return repository.getProductByName(name);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public void update(Product product) {
        repository.edit(product);
    }

    public long save(Product product, MultipartFile imageFile) throws IOException {
        Employee employee = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        product.setEmployee(employee);

        if (!imageFile.getOriginalFilename().equals("")) {
            Image image = new Image();
            String[] split = imageFile.getOriginalFilename().split("\\.");
            image.setName(split[0]);
            image.setExtension(split[1]);
            image.setPath(uploadImage(imageFile));

            product.setImage(image);
        }
        return repository.save(product);

    }

    public void delete(Product product) throws IOException {
        product = getProduct(product.getId());
        repository.delete(product);
        imageRepository.delete(product.getImage());
    }

    public String uploadImage(MultipartFile image) throws IOException {
        return imageRepository.upload(image);
    }

    public void changeImage(MultipartFile newImage, Product product) throws IOException {
        Image image = new Image();
        String[] split = newImage.getOriginalFilename().split("\\.");
        image.setName(split[0]);
        image.setExtension(split[1]);
        image.setPath(imageRepository.change(newImage, product.getImage()));

        repository.changeImage(image, product);
    }

    public List<Product> getByName(String name, String orderBy) {
        return repository.matchByName(name, orderBy);
    }

    public List<Product> getByNameAndCategory(String name, Category category, String orderBy) {
        return repository.getByNameAndCategory(name, category, orderBy);
    }

    public long getNumberOfProducts() {
        return repository.getNumberOfProducts();
    }

    public long getNumberOfLowQuantityProducts() {
        return repository.getNumberOfLowQuantityProducts();
    }

    public long getNumberOfOutOfStockProducts() {
        return repository.getNumberOfOutOfStockProducts();
    }

    public ProductRepository getRepository() {
        return repository;
    }
}
