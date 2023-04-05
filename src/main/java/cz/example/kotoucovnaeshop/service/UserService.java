package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.repository.impl.UserRepositoryImpl;
import cz.example.kotoucovnaeshop.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepositoryImpl userRepository;

    public void createUser(Client client) {
        client.setPassword(SecurityUtility.passwordEncoder().encode(client.getPassword()));
        client.addRole("USER");
        userRepository.saveUser(client);
    }
}
