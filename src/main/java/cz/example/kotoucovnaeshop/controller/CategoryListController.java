package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.CategoryService;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CategoryListController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/{categoryName}/produkty")
    public String productList(Model model, @PathVariable String categoryName) {
        System.out.println(categoryName);
        Category category = categoryService.getCategory(categoryName);
        model.addAttribute("category", category);

        List<Category> subcategories = categoryService.getSubcategories(category);
        model.addAttribute("subcategories", subcategories);

        List<Product> products = productService.getAllProductsInCategory(category);
        System.out.println(products.size());
        model.addAttribute("products", products);


        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        for (Product product :
                products) {
            System.out.println(product.getImageUrl());
        }
        return "categoryList";
    }

}
