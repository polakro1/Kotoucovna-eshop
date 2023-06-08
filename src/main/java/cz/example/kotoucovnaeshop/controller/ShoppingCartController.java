package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.*;
import cz.example.kotoucovnaeshop.service.CustomerService;
import cz.example.kotoucovnaeshop.service.ProductService;
import cz.example.kotoucovnaeshop.service.ShoppingCartService;
import cz.example.kotoucovnaeshop.service.TypesAndStatesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

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
    private CustomerService customerService;

    @Autowired
    private SmartValidator validator;

    @GetMapping("/cart")
    public String showCart(Model model) {
        return "shoppingCart";
    }

    @PostMapping("cart/add")
    public String addItem(@ModelAttribute("productid") long productid) {
        Product product = productService.getProduct(productid);
        cartService.addItem(product, 1);
        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout1")
    public String checkout1(Model model) {
        model.addAttribute("paymentTypes", typesAndStatesService.getAllPaymentTypes());
        model.addAttribute("shippingTypes", typesAndStatesService.getAllShippingTypes());

        return "checkout1";
    }

    @PostMapping("/cart/checkout1")
    public String checkout1(Long paymentTypeId,
                            Long shippingTypeId,
                            RedirectAttributes redirectAttributes,
                            HttpSession session) {
        if (paymentTypeId == null) {
            redirectAttributes.addFlashAttribute("paymentTypeIdEmpty", true);
        }
        if (shippingTypeId == null) {
            redirectAttributes.addFlashAttribute("shippingTypeIdEmpty", true);
        }
        if (paymentTypeId == null || shippingTypeId == null) {
            return "redirect:/cart/checkout1";
        }


        Order order = new Order();
        order.setOrderItems(cartService.getItemsAsOrderItems());
        order.setPaymentType(typesAndStatesService.getPaymentType(paymentTypeId));
        order.setShippingType(typesAndStatesService.getShippingType(shippingTypeId));
        session.setAttribute("order", order);

        return "redirect:/cart/checkout2";
    }

    @GetMapping("/cart/checkout2")
    public String checkout(Model model, @SessionAttribute Order order) {
        Client client = new Client();
        Adressee adressee = new Adressee();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            client = customerService.getByUsername(authentication.getName());
            adressee.setName(client.getName());
            adressee.setSurname(client.getSurname());
            adressee.setEmail(client.getEmail());
            adressee.setTel(client.getTel());
        }
        order.setClient(client);
        order.setAdressee(adressee);
        order.setShippingAdress(client.getAdress());
        order.setBillingAdress(new Adress());
        model.addAttribute(order);
        return "checkout2";
    }

    @PostMapping("/cart/checkout2")
    public String checkout(Order orderForm,
                           BindingResult bindingResult,
                           @SessionAttribute Order order,
                           boolean adressesAreSame) {
        orderForm.getShippingAdress().setPostalCode(orderForm.getShippingAdress().getPostalCode().replace(" ", ""));
        orderForm.getBillingAdress().setPostalCode(orderForm.getBillingAdress().getPostalCode().replace(" ", ""));

        if (adressesAreSame) {
            orderForm.setBillingAdress(orderForm.getShippingAdress());
        }

        validator.validate(orderForm, bindingResult);

        if (bindingResult.hasErrors()) {

            return "checkout2";
        }

        order.setAdressee(orderForm.getAdressee());
        order.setShippingAdress(orderForm.getShippingAdress());
        order.setBillingAdress(orderForm.getBillingAdress());

        return "redirect:/cart/checkout3";
    }

    @GetMapping("cart/checkout3")
    public String checkout3() {
        return "checkout3";
    }

    @PostMapping("cart/deleteItem")
    public String deleteItem(@ModelAttribute("itemIndex") int itemIndex) {
        cartService.deleteItem(itemIndex);

        return "redirect:/cart";
    }

    @PostMapping("/cart/updateQuantity")
    @ResponseBody
    public Map<String, Object> changeQuanttity(@RequestBody Map<String, Object> payload) {
        int index = Integer.parseInt((String) payload.get("cartSessionIndex"));
        int quantity = Integer.parseInt((String) payload.get("quantity"));
        CartItem cartItem = cart.getItems().get(index);

        Product product = productService.getProduct(cartItem.getProduct().getId());

        payload.put("quantity", cartService.changeQuantity(cartItem, quantity));
        payload.put("sumPrice", cart.getSumPrice());

        return payload;
    }
}
