package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Account;
import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.model.Order;
import cz.example.kotoucovnaeshop.service.OrderService;
import cz.example.kotoucovnaeshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AccountController {
    @Autowired
    CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SmartValidator validator;

    @GetMapping("/ucet")
    public String account(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = customerService.getByUsername(username);
        model.addAttribute("client", client);
        List<Order> orders = orderService.getOrdersByClient(client);
        model.addAttribute("orders", orders);

        return "account";
    }

    @PostMapping("/ucet")
    public String account( Client client, BindingResult bindingResult) {

        client.getAdress().setPostalCode(client.getAdress().getPostalCode().replace(" ", ""));

        if (!((client.getAdress().getBuildingNumber() == null || client.getAdress().getBuildingNumber().isEmpty()) &&
                (client.getAdress().getStreet() == null || client.getAdress().getStreet().isEmpty()) &&
                (client.getAdress().getCity() == null || client.getAdress().getCity().isEmpty()) &&
                (client.getAdress().getPostalCode() == null || client.getAdress().getPostalCode().isEmpty()) &&
                (client.getAdress().getCountry() == null || client.getAdress().getCountry().isEmpty()))) {
            client.setAdress(client.getAdress());
        } else {
            client.setAdress(null);
        }

        validator.validate(client, bindingResult, Account.WithoutPassword.class);

        if (bindingResult.hasErrors()) {
            return "account";
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        client.setId(customerService.getByUsername(username).getId());

        customerService.editClientDetails(client);

        return "account";
    }
}
