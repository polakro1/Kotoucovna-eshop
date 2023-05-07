package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Employee;
import cz.example.kotoucovnaeshop.repository.impl.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Employee getById(Long id) {
        return adminRepository.getById(id);
    }

    public Employee getByUsername(String username) {
        return adminRepository.getByUsername(username);
    }
}
