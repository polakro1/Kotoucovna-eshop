package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.*;
import cz.example.kotoucovnaeshop.service.ProductService;
import cz.example.kotoucovnaeshop.service.ShoppingCartService;
import cz.example.kotoucovnaeshop.service.TypesAndStatesService;
import cz.example.kotoucovnaeshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class ShoppingCartController {
    @Autowired
    private Cart cart;
    @Autowired
    private TypesAndStatesService typesAndStatesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private UserService userService;
    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("cart", cart);
        return "shoppingCart";
    }
    @PostMapping("cart/add")
    public String addItem(@ModelAttribute("productid") long productid) {
        Product product = productService.getProductById(productid);
        cartService.addItem(product,1);
        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout1")
    public String checkout1(Model model) {
        model.addAttribute("paymentTypes", typesAndStatesService.getAllPaymentTypes());
        model.addAttribute("shippingTypes", typesAndStatesService.getAllShippingTypes());

        return "checkout1";
    }

    @PostMapping("/cart/checkout1")
    public String checkout1(@ModelAttribute("paymentTypeId") long paymentTypeId,
                            @ModelAttribute("shippingTypeId") long shippingTypeId,
                            HttpSession session) {
        Order order = new Order();
        order.setOrderItems(cartService.getItemsAsOrderItems());
        order.setPaymentType(typesAndStatesService.getPaymentType(paymentTypeId));
        order.setShippingType(typesAndStatesService.getShippingType(shippingTypeId));
        session.setAttribute("order", order);
        System.out.println(paymentTypeId);
        System.out.println(order.getShippingType().getName());

        return "redirect:/cart/checkout2";
    }
    @GetMapping("/cart/checkout2")
    public String checkout(Model model) {
        Client client = new Client();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            client = userService.getByUsername(authentication.getName());
        }
        model.addAttribute("client", client);
        model.addAttribute("shippingAdress", new Adress());
        model.addAttribute("billingAdress", new Adress());
        System.out.println(client.getId());
        return "checkout2";
    }
    @PostMapping("/cart/checkout2")
    public String checkout(@ModelAttribute Client client,
                           @ModelAttribute Adress shippingAdress,
                           @ModelAttribute Adress billingAdress,
                           @SessionAttribute Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Client existingClient = userService.getByUsername(authentication.getName());
            client.setId(existingClient.getId());
        }
        order.setClient(client);
        order.setShippingAdress(shippingAdress);
        order.setBillingAdress(billingAdress);
        System.out.println(client.getEmail());
        System.out.println(shippingAdress.getCity());
        System.out.println(billingAdress.getCountry());
        System.out.println(order.getShippingType().getName());
        return "redirect:/cart/create-order";
    }

    @PostMapping("cart/deleteItem")
    public String deleteItem(@ModelAttribute("itemIndex") int itemIndex) {
        cartService.deleteItem(itemIndex);

        return "redirect:/cart";
    }
}
