package cz.example.kotoucovnaeshop.repository;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;

import javax.sound.sampled.Port;
import java.util.List;

public interface ProductRepository {
    List<Product> findAllByCategory(Category category);
    public Product getProduct(long productId);
    public List<Product> findAll();
    public void update(Product product);
    public void save(Product produc);
    public void delete(Product product);

    public List<Product> matchByName(String name);
}
