package cz.example.kotoucovnaeshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard.html";
    }

    @GetMapping("/")
    public String homePage() {
        return "index.html";
    }
}
