package cz.example.kotoucovnaeshop.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(Product product, int quantity) {
        items.add(new CartItem(product, quantity));
    }

    public double getSumPrice() {
        double sumPrice = 0;
        if (items.size() > 0) {
            for (CartItem item:
                    items) {
                sumPrice += item.getProduct().getPrice();
            }
        }
        return sumPrice;
    }
}
