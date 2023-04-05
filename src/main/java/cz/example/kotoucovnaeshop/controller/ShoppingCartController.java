package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Adress;
import cz.example.kotoucovnaeshop.model.Cart;
import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.ProductService;
import cz.example.kotoucovnaeshop.service.ShoppingCartService;
import cz.example.kotoucovnaeshop.service.TypesAndStatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ShoppingCartController {
    @Autowired
    private Cart cart;
    @Autowired
    private TypesAndStatesService typesAndStatesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService Cartservice;
    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("cart", cart);
        return "shoppingCart";
    }
    @PostMapping("cart/add")
    public String addItem(@ModelAttribute("productid") long productid) {
        Product product = productService.getProductById(productid);
        Cartservice.addItem(product,1);
        return "redirect:/cart";
    }

    @GetMapping("/checkout1")
    public String checkout1(Model model) {
        model.addAttribute("paymentTypes", typesAndStatesService.getAllPaymentTypes());
        model.addAttribute("shippingTypes", typesAndStatesService.getAllShippingTypes());

        return "checkout1";
    }

    @PostMapping("/checkout1")
    public String checkout1(@ModelAttribute("paymentTypeId") long paymentTypeId,
                            @ModelAttribute("shippingTypeId") long shippingTypeId) {
        System.out.println(paymentTypeId);
        System.out.println(shippingTypeId);

        return "redirect:checkout2";
    }
    @GetMapping("/checkout2")
    public String checkout(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("shippingAdress", new Adress());
        model.addAttribute("billingAdress", new Adress());

        return "checkout2";
    }
    @PostMapping("checkout2")
    public String checkout(@ModelAttribute Client client,
                           @ModelAttribute Adress shippingAdress,
                           @ModelAttribute Adress billingAdress) {
        System.out.println(client.getEmail());
        System.out.println(shippingAdress.getCity());
        System.out.println(billingAdress.getCountry());

        return "categoryList";
    }
}
