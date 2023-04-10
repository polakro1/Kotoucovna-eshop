package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Adress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AdressRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    private void initializeSimpleJdbcInsert() {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("adresy")
                .usingGeneratedKeyColumns("adresaid");
    }

    public Adress getAdress(long adressId) {
        Adress adress = jdbcTemplate.queryForObject(
                "select cislo_popisne, mesto, psc, ulice, zeme, adresaid from adresy " +
                        "where adresaid = ?",
                (rs, rowNum) -> {
                    Adress newAdress = new Adress(
                            rs.getLong("adresaid"),
                            rs.getString("ulice"),
                            rs.getString("cislo_popisne"),
                            rs.getString("mesto"),
                            rs.getString("psc"),
                            rs.getString("zeme")
                    );
                    return newAdress;
                }, adressId
        );
        return adress;
    }

    public Long saveAdress(Adress adress) {
        /*
        jdbcTemplate.update("insert into adresy ulice, cislo_popisne, mesto, psc, zeme" +
                "values ?,?,?,?,?",
                adress.getStreet(),
                adress.getBuildingNumber(),
                adress.getCity(),
                adress.getPostalCode(),
                adress.getCountry());
         */
        if (simpleJdbcInsert == null) {
            initializeSimpleJdbcInsert();
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ulice", adress.getStreet());
        parameters.put("cislo_popisne", adress.getBuildingNumber());
        parameters.put("mesto", adress.getCity());
        parameters.put("psc", adress.getPostalCode());
        parameters.put("zeme", adress.getCountry());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
