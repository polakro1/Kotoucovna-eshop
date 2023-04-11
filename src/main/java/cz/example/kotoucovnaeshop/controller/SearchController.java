package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.repository.ProductRepository;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public String search(@RequestParam String search, Model model) {
        model.addAttribute("searchValue", search);
        model.addAttribute("products", productService.getMatchedByName(search));

        return "search";
    }
}
