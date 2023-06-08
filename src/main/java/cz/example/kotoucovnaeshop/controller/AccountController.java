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
    public String account(Client clientForm, BindingResult bindingResult, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = customerService.getByUsername(username);
        model.addAttribute("client", client);
        List<Order> orders = orderService.getOrdersByClient(client);
        model.addAttribute("orders", orders);

        clientForm.getAdress().setPostalCode(clientForm.getAdress().getPostalCode().replace(" ", ""));

        if (!((clientForm.getAdress().getBuildingNumber() == null || clientForm.getAdress().getBuildingNumber().isEmpty()) &&
                (clientForm.getAdress().getStreet() == null || clientForm.getAdress().getStreet().isEmpty()) &&
                (clientForm.getAdress().getCity() == null || clientForm.getAdress().getCity().isEmpty()) &&
                (clientForm.getAdress().getPostalCode() == null || clientForm.getAdress().getPostalCode().isEmpty()) &&
                (clientForm.getAdress().getCountry() == null || clientForm.getAdress().getCountry().isEmpty()))) {
            clientForm.setAdress(clientForm.getAdress());
        } else {
            clientForm.setAdress(null);
        }

        validator.validate(clientForm, bindingResult, Account.WithoutPassword.class);

        if (bindingResult.hasErrors()) {
            return "account";
        }

        customerService.editClientDetails(clientForm);

        return "account";
    }
}
