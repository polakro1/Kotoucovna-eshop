package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.repository.impl.CustomerRepository;
import cz.example.kotoucovnaeshop.util.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository userRepository;

    public void createUser(Client client) {
        client.setPassword(SecurityUtility.passwordEncoder().encode(client.getPassword()));
        client.addRole("USER");
        saveUser(client);
    }

    public Long saveUser(Client client) {
        return userRepository.saveUser(client);
    }

    public void editClientDetails(Client client) {
        userRepository.editClientDetails(client);
    }

    public Client getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Client getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    public void changePassword(Client client, String newPassword) {
        userRepository.changePassword(client, SecurityUtility.passwordEncoder().encode(newPassword));
    }
}
