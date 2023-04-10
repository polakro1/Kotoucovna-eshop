package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.*;
import cz.example.kotoucovnaeshop.service.AdressService;
import cz.example.kotoucovnaeshop.service.ProductService;
import cz.example.kotoucovnaeshop.service.TypesAndStatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    @Autowired
    private ProductService productService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private TypesAndStatesService typesAndStatesService;
    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private AdminRepositoryImpl adminRepository;
    private void initializeSimpleJdbcInsert() {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("objednavky")
                .usingGeneratedKeyColumns("objednavkaid");
    }

    private final RowMapper<Order> orderRowMapper = (resultSet, rowNum) -> {
        Order order = new Order();
        order.setId(resultSet.getLong("objednavkaid"));
        order.setShippingDate(resultSet.getDate("datum_dodani"));
        order.setOrderDate(resultSet.getTimestamp("datum_objednani").toLocalDateTime());
        order.setShippingAdress(
                adressService.getAdress(resultSet.getLong("adresa_dodani"))
        );
        order.setBillingAdress(
                adressService.getAdress(resultSet.getLong("adresa_fakturace")));
        order.setPaymentType(typesAndStatesService.getPaymentType(resultSet.getLong("typy_platbyid")));
        order.setShippingType(typesAndStatesService.getShippingType(resultSet.getLong("typ_dopravyid")));
        order.setOrderState(typesAndStatesService.getOrderState(resultSet.getLong("stav_objednavkyid")));
        order.setClient(userRepository.findById(resultSet.getLong("zakaznikid")));
        if (resultSet.getString("zamestnanecid") != null) {
            order.setEmployee(adminRepository.getById(resultSet.getLong("zamestnanecid")));
        } else {
            order.setEmployee(null);
        }
        order.setOrderItems(getOrderItems(order));

        return order;
    };
    //To doo
    public List<Order> getAllOrders() {
        List<Order> orders = jdbcTemplate.query(
                "select objednavkaid, datum_dodani, datum_objednani, adresa_dodani,adresa_fakturace," +
                        "typy_platbyid, typ_dopravyid, stav_objednavkyid, zakaznikid, zamestnanecid from objednavky" ,
                orderRowMapper);
        return orders;
    }

    public Long saveOrder(Order order) {
        Long billingAdressId = null;
        if (order.getBillingAdress() != null) {
            billingAdressId = adressService.saveAdress(order.getBillingAdress());
        }
        /*
        jdbcTemplate.update("insert into objednavky (datum_dodani, datum_objednani, adresa_dodani, adresa_fakturace, " +
                "typ_dopravyid, typy_platbyid, stav_objednavkyid, zakaznikid)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)",
                order.getShippingDate(),
                LocalDateTime.now(),
                adressService.saveAdress(order.getShippingAdress()),
                billingAdressId,
                order.getShippingType().getId(),
                order.getPaymentType().getId(),
                order.getOrderState().getId(),
                order.getClient().getId()
                );

         */
        if (simpleJdbcInsert == null) {
            initializeSimpleJdbcInsert();
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("datum_dodani", order.getShippingDate());
        parameters.put("datum_objednani", LocalDateTime.now());
        parameters.put("adresa_dodani", adressService.saveAdress(order.getShippingAdress()));
        parameters.put("adresa_fakturace", billingAdressId);
        parameters.put("typ_dopravyid", order.getShippingType().getId());
        parameters.put("typy_platbyid", order.getPaymentType().getId());
        parameters.put("stav_objednavkyid", order.getOrderState().getId());
        parameters.put("zakaznikid", order.getClient().getId());
        order.setId(simpleJdbcInsert.executeAndReturnKey(parameters).longValue());
        saveOrderItems(order);

        return order.getId();
    }

    private void saveOrderItems(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem :
                orderItems) {
            jdbcTemplate.update(
                    "insert into polozky_objednavky (cena, mnozstvi, objednavkyid, produktyid)" +
                            "values (?, ?, ?, ?)",
                    orderItem.getPrice(),
                    orderItem.getQuantity(),
                    order.getId(),
                    orderItem.getProduct().getId()
            );
        }
    }
    private List<OrderItem> getOrderItems(Order order) {
        List<OrderItem> orderItems = jdbcTemplate.query(
                "select polozky_objednavkyid, cena, mnozstvi, produktyId from polozky_objednavky " +
                        "where objednavkyid = ?",
                (rs, rowNum) -> {
                    OrderItem orderItem = new OrderItem(
                            rs.getLong("polozky_objednavkyid"),
                            productService.getProductById(rs.getLong("produktyid")),
                            rs.getInt("mnozstvi"),
                            rs.getDouble("cena")
                    );
                    return orderItem;
                }, order.getId());
        return orderItems;
    }

    public Order getOrderById(long id) {
        Order order = jdbcTemplate.queryForObject(
                "select objednavkaid, datum_dodani, datum_objednani, adresa_dodani,adresa_fakturace, " +
                        "typy_platbyid, typ_dopravyid, stav_objednavkyid, zakaznikid, zamestnanecid from objednavky " +
                        "where objednavkaid = ?",
                orderRowMapper,
                id
        );
        return order;
    }

    public void updateOrderState(Order order, OrderState orderState) {
        jdbcTemplate.update("update objednavky set stav_objednavkyid = ? where objednavkaid = ?",
                orderState.getId(),
                order.getId());
    }

    public void setConfirmEmployee(Order order) {
        jdbcTemplate.update("update objednavky set zamestnanecid = ? where objednavkaid = ?",
                order.getEmployee().getId(),
                order.getId());
    }

}
