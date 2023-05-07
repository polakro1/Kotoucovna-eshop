package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.CartItem;
import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ShoppingCartRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductService productService;

    public List<CartItem> getCartItems(Client client) {
        List<CartItem> items = jdbcTemplate.query(
                "select polozky_kosikuid, mnozstvi, zakazniciid, produktyid from polozky_kosiku " +
                        "where zakazniciid = ?",
                (rs, rowNum) -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setId(rs.getLong("polozky_kosikuid"));
                    cartItem.setQuantity(rs.getInt("mnozstvi"));
                    cartItem.setProduct(productService.getProduct(rs.getLong("produktyid")));

                    return cartItem;
                }, client.getId()
        );
        return items;
    }

    public void addItem(Product product, int quantity, Client client) {
        jdbcTemplate.update(
                "insert into polozky_kosiku (mnozstvi, zakazniciid, produktyid) " +
                        "values (?, ?, ?)",
                quantity,
                client.getId(),
                product.getId()
        );
    }

    public void deleteItem(CartItem item) {
        jdbcTemplate.update("delete from polozky_kosiku where polozky_kosikuid = ?",
                item.getId());
    }

    public void clear(Client client) {
        jdbcTemplate.update("delete from polozky_kosiku where zakazniciid = ?",
                client.getId());
    }

    public void changeQuantity(CartItem cartItem, int quantity) {
        jdbcTemplate.update(
                "update polozky_kosiku set mnozstvi = ? where polozky_kosikuid = ?",
                quantity,
                cartItem.getId()
        );
    }
}
