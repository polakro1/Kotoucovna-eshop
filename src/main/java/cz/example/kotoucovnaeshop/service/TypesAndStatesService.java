package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.PaymentType;
import cz.example.kotoucovnaeshop.model.ShippingType;
import cz.example.kotoucovnaeshop.repository.impl.TypesAndStatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypesAndStatesService {
    @Autowired
    private TypesAndStatesRepository repository;

    public List<PaymentType> getAllPaymentTypes() {
        return repository.getAllPaymentTypes();
    }

    public List<ShippingType> getAllShippingTypes() {
        return repository.getAllShippingTypes();
    }
}
