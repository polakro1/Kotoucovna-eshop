package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.OrderState;
import cz.example.kotoucovnaeshop.model.PaymentType;
import cz.example.kotoucovnaeshop.model.ShippingType;
import cz.example.kotoucovnaeshop.repository.impl.TypesAndStatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypesAndStatesService {
    public static final Long ORDER_CREATED = 1L;
    public static final Long ORDER_CONFIRMED = 2L;
    public static final Long ORDER_SHIPPED = 3L;
    public static final Long ORDER_DELIVERED = 4L;
    @Autowired
    private TypesAndStatesRepository repository;

    public List<PaymentType> getAllPaymentTypes() {
        return repository.getAllPaymentTypes();
    }

    public List<ShippingType> getAllShippingTypes() {
        return repository.getAllShippingTypes();
    }

    public PaymentType getPaymentType(long id) {
        return repository.getPaymentType(id);
    }

    public ShippingType getShippingType(long id) {
        return repository.getShippingType(id);
    }

    public OrderState getOrderState(long id) {
        return repository.getOrderState(id);
    }
}
