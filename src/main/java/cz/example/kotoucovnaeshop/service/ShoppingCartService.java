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
    private CustomerService customerService;


    public void addItem (Product product, int quantity) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            Client client = customerService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            shoppingCartRepository.addItem(product, quantity, client);
        }
        cart.addItem(product, quantity);
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
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            Client client = customerService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            shoppingCartRepository.clear(client);
        }

        cart.getItems().clear();
    }
    public void deleteItem(int index) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            shoppingCartRepository.deleteItem(cart.getItems().get(index));
        }
        cart.getItems().remove(index);
    }

    public void setCartByClient(Client client) {
        cart.setItems(shoppingCartRepository.getCartItems(client));
    }

    public boolean isInCart(Product product) {
        return cart.getItems().stream().anyMatch(item -> item.getProduct().getId() == product.getId());
    }

    public void changeQuantity(CartItem cartItem, int newQuantity) {
        cartItem.setQuantity(newQuantity);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            shoppingCartRepository.changeQuantity(cartItem, newQuantity);
        }
    }
}
