package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TestController {
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard.html";
    }
}
