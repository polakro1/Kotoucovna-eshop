package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.*;
import cz.example.kotoucovnaeshop.service.AdressService;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class OrderRepositoryImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductService productService;
    @Autowired
    private AdressService adressService;
    //To doo
    public List<Order> getAllOrders() {
        List<Order> orders = jdbcTemplate.query(
                "select objednavkaid, datum_dodani, datum_objednani, adresa_dodani,adresa_fakturace, zakaznikid from produkty" ,
                (rs, rowNum) -> {
                    Order order = new Order();
                    order.setId(rs.getLong("objednavkaid"));
                    order.setShippingDate(rs.getDate("datum_dodani"));
                    order.setOrderDate(rs.getDate("datum_objednani"));
                    order.setShippingAdress(
                            adressService.getAdress(rs.getLong("adresa_dodani"))
                    );
                    order.setBillingAdress(
                            adressService.getAdress(rs.getLong("adresa_fakturace")));
                    order.setKlient(new Client());
                    order.setOrderItems(getOrderItems(order));

                    return order;
                });
        return orders;
    }

    private List<OrderItem> getOrderItems(Order order) {
        List<OrderItem> orderItems = jdbcTemplate.query(
                "select polozky_objednavkyid, cena, mnozstvi, produktyId from polozky_objednavky" +
                        "where objednavkaid = ?",
                (rs, rowNum) -> {
                    OrderItem orderItem = new OrderItem(
                            rs.getLong("polozky_objednavkyid"),
                            productService.getProductById(rs.getLong("produktid")),
                            rs.getInt("mnozstvi"),
                            rs.getDouble("cena")
                    );
                    return orderItem;
                }, order.getId());
        return orderItems;
    }

}
