package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Cart;
import cz.example.kotoucovnaeshop.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private Cart cart;


    public void addItem (Product product, int quantity) {
        cart.addItem(product, quantity);
    }

    public Cart getCartFromSession() {
        Cart cart = (Cart) httpSession.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            httpSession.setAttribute("cart", cart);
        }
        return cart;
    }
}
