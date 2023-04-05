package cz.example.kotoucovnaeshop.repository;

import cz.example.kotoucovnaeshop.model.Category;

import java.util.List;

public interface CategoryRepository {
    public List<Category> getAllCategories();
    public Category getById(long id);
    public Category getByName(String name);
    public List<Category> getSubcategories(Category category);
}
