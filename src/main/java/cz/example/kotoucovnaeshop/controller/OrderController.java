package cz.example.kotoucovnaeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("admin/orders")
    public String manageOrders(Model model) {
        return null;
    }
}
