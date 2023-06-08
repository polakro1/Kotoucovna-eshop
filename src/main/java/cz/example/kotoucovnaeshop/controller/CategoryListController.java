package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.CategoryService;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CategoryListController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{categoryName}/produkty")
    public String productList(Model model, @PathVariable String categoryName, String sortBy) {
        Category category = categoryService.getCategoryByUrl(categoryName);
        model.addAttribute("category", category);


        List<Category> subcategories = categoryService.getSubcategories(category);
        model.addAttribute("subcategories", subcategories);

        if (sortBy == null) {
            sortBy = productService.getRepository().NAME_ASC;
        }
        model.addAttribute("sortBy", sortBy);
        List<Product> products = productService.getAllProductsInCategory(category, sortBy);
        model.addAttribute("products", products);

        return "categoryList";
    }


}
