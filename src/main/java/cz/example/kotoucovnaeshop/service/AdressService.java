package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Adress;
import cz.example.kotoucovnaeshop.repository.impl.AdressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdressService {
    @Autowired
    private AdressRepository repository;

    public Adress getAdress(long adressId) {
        return repository.getAdress(adressId);
    }
}
