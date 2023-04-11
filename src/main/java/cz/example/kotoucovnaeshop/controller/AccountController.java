package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Cart;
import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.model.Order;
import cz.example.kotoucovnaeshop.repository.impl.UserRepositoryImpl;
import cz.example.kotoucovnaeshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private OrderService orderService;

    @GetMapping("/ucet")
    public String account(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = userRepository.findByUsername(username);
        model.addAttribute("client", client);
        List<Order> orders = orderService.getOrdersByClient(client);
        model.addAttribute("orders", orders);

        return "account.html";
    }
}
