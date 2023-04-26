package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.*;
import cz.example.kotoucovnaeshop.repository.impl.ShoppingCartRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private Cart cart;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private UserService userService;


    public void addItem (Product product, int quantity) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Client client = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            shoppingCartRepository.addItem(product, quantity, client);
        }
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

    public void clearCart() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Client client = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            shoppingCartRepository.clear(client);
        }

        cart.getItems().clear();
    }
    public void deleteItem(int index) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            shoppingCartRepository.deleteItem(cart.getItems().get(index));
        }
        cart.getItems().remove(index);
    }

    public void setCartByClient(Client client) {
        cart.setItems(shoppingCartRepository.getCartItems(client));
    }
}
