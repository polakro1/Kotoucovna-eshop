package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Adress;
import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.service.UserDetailsServiceImpl;
import cz.example.kotoucovnaeshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClientController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("adress", new Adress());
        return "registration.html";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("client") Client client,
                               @ModelAttribute("adress") Adress adress) {
        System.out.println(client.getPassword());
        userService.createUser(client);
        userDetailsService.authenticateUser(client.getUsername());

        return "redirect:login";
    }
}
