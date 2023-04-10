package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Adress;
import cz.example.kotoucovnaeshop.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    @Autowired
    private AdressRepository adressRepository;
    private void initializeSimpleJdbcInsert() {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("zakaznici")
                .usingGeneratedKeyColumns("zakaznikid");
    }

    private final RowMapper<Client> clientRowMapper = (resultSet, rowNum) -> {
        Client newClient = new Client();
        newClient.setId(resultSet.getLong("zakaznikid"));
        newClient.setUsername(resultSet.getString("prihlasovaci_jmeno"));
        newClient.setEmail(resultSet.getString("email"));
        newClient.setPassword(resultSet.getString("heslo"));
        newClient.setName(resultSet.getString("jmeno"));
        newClient.setSurname(resultSet.getString("prijmeni"));
        newClient.setTel(resultSet.getString("tel"));
        if (resultSet.getString("adresaid") != null) {
            Adress adress = adressRepository.getAdress(resultSet.getLong("adresaid"));
            newClient.setAdress(adress);
        } else {
            newClient.setAdress(null);
        }
        newClient.addRole("USER");
        return newClient;
    };
    public Client findByUsername(String username) {
        Client client = jdbcTemplate.queryForObject(
                "select zakaznikid, prihlasovaci_jmeno, email, heslo, jmeno, prijmeni, tel, adresaid from zakaznici " +
                        "where prihlasovaci_jmeno = ?",
                clientRowMapper,
                username);
        return client;
    }

    public Long saveUser(Client client) {
        if (simpleJdbcInsert == null) {
            initializeSimpleJdbcInsert();
        }
        Long adressId = null;
        if (client.getAdress() != null) {
            adressId = adressRepository.saveAdress(client.getAdress());
        }
        System.out.println(client.getPassword());
        String sql = "insert into zakaznici (email, prihlasovaci_jmeno, heslo, jmeno, prijmeni, tel, adresaid)" +
                " values (?,?,?,?,?,?,?)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", client.getEmail());
        parameters.put("prihlasovaci_jmeno", client.getUsername());
        parameters.put("heslo", client.getPassword());
        parameters.put("jmeno", client.getName());
        parameters.put("prijmeni", client.getSurname());
        parameters.put("tel", client.getTel());
        parameters.put("adresaid", adressId);
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public Client findById(long id) {
        Client client = jdbcTemplate.queryForObject(
                "select zakaznikid, prihlasovaci_jmeno, email, heslo, jmeno, prijmeni, tel, adresaid from zakaznici " +
                        "where zakaznikid = ?",
                clientRowMapper,
                id
        );
        return client;
    }
}
