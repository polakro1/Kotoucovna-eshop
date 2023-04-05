package cz.example.kotoucovnaeshop.repository.impl;

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
}
