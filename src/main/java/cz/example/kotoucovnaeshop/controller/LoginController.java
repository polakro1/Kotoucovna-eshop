package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login-user")
    String login() {
        return "login.html";
    }

    @GetMapping("/admin/login")
    String adminLogin() {
        return "admin/adminLogin";
    }
}
