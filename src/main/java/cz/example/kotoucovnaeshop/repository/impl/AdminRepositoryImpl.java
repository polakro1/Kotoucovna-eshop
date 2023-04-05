package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Employee;
import cz.example.kotoucovnaeshop.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepositoryImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Employee getByUsername(String username) {
        Employee employee = jdbcTemplate.queryForObject(
                "select email, prihlasovaci_jmeno, heslo, jmeno, prijmeni from zamestnanci " +
                        "where prihlasovaci_jmeno = ?",
                (rs, rowNum) -> {
                    System.out.println(SecurityUtility.passwordEncoder().encode(rs.getString("heslo")));
                    Employee newEmployee = new Employee();
                    newEmployee.setEmail(rs.getString("email"));
                    newEmployee.setUsername(rs.getString("prihlasovaci_jmeno"));
                    newEmployee.setPassword(rs.getString("heslo"));
                    newEmployee.setName(rs.getString("jmeno"));
                    newEmployee.setSurname(rs.getString("prijmeni"));
                    newEmployee.addRole("ADMIN");
                    return newEmployee;
                }, username);
        return employee;
    }
}
