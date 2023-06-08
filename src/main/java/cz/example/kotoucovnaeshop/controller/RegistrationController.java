package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Adress;
import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.service.CustomerDetailsService;
import cz.example.kotoucovnaeshop.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SmartValidator validator;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("adress", new Adress());

        return "registration.html";
    }

    @PostMapping("/registration")
    public String registration(@Valid Client client,
                               BindingResult clientBindingResult,
                               Adress adress,
                               BindingResult adressBindingResult) {
        adress.setPostalCode(adress.getPostalCode().replace(" ", ""));
        if (!((adress.getBuildingNumber() == null || adress.getBuildingNumber().isEmpty()) &&
                (adress.getStreet() == null || adress.getStreet().isEmpty()) &&
                (adress.getCity() == null || adress.getCity().isEmpty()) &&
                (adress.getPostalCode() == null || adress.getPostalCode().isEmpty()) &&
                (adress.getCountry() == null || adress.getCountry().isEmpty()))) {
            validator.validate(adress, adressBindingResult);
            client.setAdress(adress);
        } else {
            client.setAdress(null);
        }

        try {
            customerService.getByEmail(client.getEmail());
            clientBindingResult.rejectValue("email", "error.client.email.exist", "Email byl již použit.");
        } catch (EmptyResultDataAccessException e) {
        }
        try {
            customerService.getByUsername(client.getUsername());
            clientBindingResult.rejectValue("username", "error.client.username.exist", "Přihlašovací jméno již existuje.");
        } catch (EmptyResultDataAccessException e) {
        }

        if (clientBindingResult.hasErrors() || adressBindingResult.hasErrors()) {
            return "registration";
        }


        customerService.createUser(client);
        customerDetailsService.authenticateUser(client.getUsername());

        return "redirect:login";
    }
}
