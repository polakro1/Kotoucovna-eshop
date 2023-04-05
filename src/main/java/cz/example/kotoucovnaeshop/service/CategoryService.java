package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory(long id) {
        return categoryRepository.getById(id);
    }
    public Category getCategory(String name) {
        return categoryRepository.getByName(name);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    public List<Category> getSubcategories(Category category){return categoryRepository.getSubcategories(category);}
}
