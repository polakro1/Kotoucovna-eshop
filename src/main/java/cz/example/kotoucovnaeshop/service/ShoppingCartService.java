package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Cart;
import cz.example.kotoucovnaeshop.model.CartItem;
import cz.example.kotoucovnaeshop.model.OrderItem;
import cz.example.kotoucovnaeshop.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<OrderItem> getItemsAsOrderItems() {
        List<CartItem> cartItems = cart.getItems();
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem :
                cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            orderItems.add(orderItem);
        }

        return orderItems;
    }
}
