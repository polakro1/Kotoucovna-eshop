package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public String search(@RequestParam String search, Model model, String sortBy) {
        model.addAttribute("searchValue", search);

        if (sortBy == null) {
            sortBy = productService.getRepository().NAME_ASC;
        }
        model.addAttribute("sortBy", sortBy);

        List<Product> products = productService.getByName(search, sortBy);
        model.addAttribute("products", products);

        return "search";
    }
}
