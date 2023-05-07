package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SmartValidator validator;

    @GetMapping("/login")
    String login() {
        return "login.html";
    }

    @GetMapping("/admin/login")
    String adminLogin() {
        return "admin/adminLogin";
    }

    @GetMapping("/password-reset")
    public String passwordReset(Model model) {
        model.addAttribute("client", new Client());

        return "passwordReset";
    }

    @PostMapping("/password-reset")
    public String passwordReset(Client client, BindingResult bindingResult, Model model) {
        String newPassword = client.getPassword();
        try {
            client = customerService.getByEmail(client.getEmail());
            client.setPassword(newPassword);
        } catch (EmptyResultDataAccessException e) {
            bindingResult.rejectValue("email", "error.client.email.notExist", "Tento email není znám.");
        }

        validator.validate(client, bindingResult);

        if (bindingResult.hasErrors()) {
            return "passwordReset";
        }


        customerService.changePassword(client, newPassword);

        return "redirect:/login";
    }

}
