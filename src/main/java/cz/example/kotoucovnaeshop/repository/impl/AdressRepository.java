package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Adress;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AdressRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall procSaveAdress;

    private final RowMapper<Adress> adressRowMapper = (rs, rowNum) -> {
        Adress adress = new Adress();
        adress.setId(rs.getLong("adresaid"));
        adress.setStreet(rs.getString("ulice"));
        adress.setBuildingNumber(rs.getString("cislo_popisne"));
        adress.setCity(rs.getString("mesto"));
        adress.setPostalCode(rs.getString("psc"));
        adress.setCountry(rs.getString("zeme"));

        return adress;
    };

    @PostConstruct
    private void initProcSaveAdress() {
        this.procSaveAdress = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("adresy");
    }

    public Adress getAdress(long adressId) {
        Adress adress = jdbcTemplate.queryForObject(
                "select cislo_popisne, mesto, psc, ulice, zeme, adresaid from adresy " +
                        "where adresaid = ?",
                adressRowMapper,
                adressId
        );
        return adress;
    }

    public Long saveAdress(Adress adress) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_ulice", adress.getStreet())
                .addValue("in_cislo_popisne", adress.getBuildingNumber())
                .addValue("in_mesto", adress.getCity())
                .addValue("in_psc", adress.getPostalCode())
                .addValue("in_zeme", adress.getCountry());

        Map out = procSaveAdress.execute(in);

        BigDecimal orderId = (BigDecimal) out.get("OUT_ADRESAID");
        return Long.valueOf(orderId.longValue());
    }
}
