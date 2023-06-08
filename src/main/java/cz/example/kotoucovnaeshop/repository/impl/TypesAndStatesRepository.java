package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.OrderState;
import cz.example.kotoucovnaeshop.model.PaymentType;
import cz.example.kotoucovnaeshop.model.ShippingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TypesAndStatesRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PaymentType> getAllPaymentTypes() {
        List<PaymentType> paymentTypes = jdbcTemplate.query(
                "select nazev, typy_platbyid, cena from typy_platby",
                (rs, rowNum) -> {
                    PaymentType paymentType = new PaymentType(
                            rs.getLong("typy_platbyid"),
                            rs.getString("nazev"),
                            rs.getInt("cena")
                    );
                    return paymentType;
                }
        );
        return paymentTypes;
    }

    public List<ShippingType> getAllShippingTypes() {
        List<ShippingType> shippingTypes = jdbcTemplate.query(
                "select nazev, typ_dopravyid, cena from typ_dopravy",
                (rs, rowNum) -> {
                    ShippingType shippingType = new ShippingType(
                            rs.getLong("typ_dopravyid"),
                            rs.getString("nazev"),
                            rs.getInt("cena")
                    );
                    return shippingType;
                }
        );
        return shippingTypes;
    }

    public PaymentType getPaymentType(long id) {
        PaymentType paymentType = jdbcTemplate.queryForObject(
                "select nazev, typy_platbyid, cena from typy_platby where typy_platbyid = ?",
                (rs, rowNum) -> {
                    PaymentType newPaymentType = new PaymentType(
                            rs.getLong("typy_platbyid"),
                            rs.getString("nazev"),
                            rs.getInt("cena")
                    );
                    return newPaymentType;
                }, id);
        return paymentType;
    }

    public ShippingType getShippingType(long id) {
        ShippingType shippingType = jdbcTemplate.queryForObject(
                "select nazev, typ_dopravyid, cena from typ_dopravy where typ_dopravyid = ?",
                (rs, rowNum) -> {
                    ShippingType newShippingType = new ShippingType(
                            rs.getLong("typ_dopravyid"),
                            rs.getString("nazev"),
                            rs.getInt("cena")
                    );
                    return newShippingType;
                }, id);
        return shippingType;
    }

    public OrderState getOrderState(long id) {
        OrderState orderState = jdbcTemplate.queryForObject(
                "select stav_objednavkyid, nazev from stavy_objednavky where stav_objednavkyid = ?",
                (rs, rowNum) -> {
                    OrderState newOrderState = new OrderState();
                    newOrderState.setId(rs.getLong("stav_objednavkyid"));
                    newOrderState.setName(rs.getString("nazev"));

                    return newOrderState;
                }, id
        );
        return orderState;
    }

}
