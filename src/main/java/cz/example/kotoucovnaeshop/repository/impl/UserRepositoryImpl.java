package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Adress;
import cz.example.kotoucovnaeshop.model.Client;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class UserRepositoryImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AdressRepository adressRepository;

    public Client findByUsername(String username) {
        Client client = jdbcTemplate.queryForObject(
                "select prihlasovaci_jmeno, email, heslo, jmeno, prijmeni, tel, adresaid from zakaznici " +
                        "where prihlasovaci_jmeno = ?",
                (rs, rowNum) -> {
                    Client newClient = new Client();
                    newClient.setUsername(rs.getString("prihlasovaci_jmeno"));
                    newClient.setEmail(rs.getString("email"));
                    newClient.setPassword(rs.getString("heslo"));
                    newClient.setName(rs.getString("jmeno"));
                    newClient.setSurname(rs.getString("prijmeni"));
                    newClient.setTel(rs.getString("tel"));
                    if (rs.getString("adresaid") != null) {
                        Adress adress = adressRepository.getAdress(rs.getLong("adresaid"));
                        newClient.setAdress(adress);
                    } else {
                        newClient.setAdress(null);
                    }
                    newClient.addRole("USER");
                    return newClient;
                }, username);
        return client;
    }

    public void saveUser(Client client) {
        Long adressId = null;
        if (client.getAdress() != null) {
            adressId = adressRepository.saveAdress(client.getAdress());
        }
        System.out.println(client.getPassword());
        jdbcTemplate.update("" +
                "insert into zakaznici (email, prihlasovaci_jmeno, heslo, jmeno, prijmeni, tel, adresaid)" +
                        " values (?,?,?,?,?,?,?)",
                client.getEmail(),
                client.getUsername(),
                client.getPassword(),
                client.getName(),
                client.getSurname(),
                client.getTel(),
                adressId
                );
    }
}
