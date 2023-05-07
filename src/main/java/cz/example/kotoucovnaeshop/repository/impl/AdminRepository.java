package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Employee;
import cz.example.kotoucovnaeshop.util.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<Employee> employeeRowMapper = ((rs, rowNum) -> {
        Employee newEmployee = new Employee();
        newEmployee.setEmail(rs.getString("email"));
        newEmployee.setUsername(rs.getString("prihlasovaci_jmeno"));
        newEmployee.setPassword(rs.getString("heslo"));
        newEmployee.setName(rs.getString("jmeno"));
        newEmployee.setSurname(rs.getString("prijmeni"));
        newEmployee.addRole("ADMIN");
        return newEmployee;
    });
    public Employee getByUsername(String username) {
        Employee employee = jdbcTemplate.queryForObject(
                "select zamestnanecid, email, prihlasovaci_jmeno, heslo, jmeno, prijmeni from zamestnanci " +
                        "where prihlasovaci_jmeno = ?",
                (rs, rowNum) -> {
                    System.out.println(SecurityUtility.passwordEncoder().encode(rs.getString("heslo")));
                    Employee newEmployee = new Employee();
                    newEmployee.setId(rs.getLong("zamestnanecid"));
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

    public Employee getById(long id) {
        Employee employee = jdbcTemplate.queryForObject(
                "select zamestnanecid, email, prihlasovaci_jmeno, heslo, jmeno, prijmeni from zamestnanci " +
                        "where zamestnanecid = ?",
                employeeRowMapper,
                id
        );
        return employee;
    }
}
