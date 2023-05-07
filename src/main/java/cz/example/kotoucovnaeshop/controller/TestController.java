package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Cart;
import cz.example.kotoucovnaeshop.model.CartItem;
import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.ProductService;
import cz.example.kotoucovnaeshop.util.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Soundbank;
import java.util.List;

@Controller
public class TestController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard.html";
    }

    @GetMapping("/")
    public String homePage() {
        System.out.println(passwordEncoder.encode("AdminHeslo3"));
        return "index.html";
    }
}
