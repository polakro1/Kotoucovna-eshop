package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.repository.CategoryRepository;
import cz.example.kotoucovnaeshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<Product> findAllByCategory(Category category) {
        List<Product> products = jdbcTemplate.query(
                "select produktid, cena, mnozstvi, nazev, popis, popis_strucny, obrazek from produkty " +
                        "where kategorieid = ? or " +
                        "kategorieid in (select kategorieid from kategorie where nadkategorie = ?)" ,
                (rs, rowNum) -> {
                    Product newProduct = new Product(
                            rs.getLong("produktid"),
                            rs.getDouble("cena"),
                            rs.getInt("mnozstvi"),
                            rs.getString("nazev"),
                            rs.getString("popis_strucny"),
                            rs.getString("popis"),
                            rs.getString("obrazek"),
                            category
                    );
                    return newProduct;
                }, category.getId(), category.getId());
        return products;
    }

    public List<Product> findAll() {
        List<Product> products = jdbcTemplate.query(
                "select produktid, cena, mnozstvi, nazev, popis, popis_strucny, kategorieid, obrazek from produkty" ,
                (rs, rowNum) -> {
                    Product newProduct = new Product(
                            rs.getLong("produktid"),
                            rs.getDouble("cena"),
                            rs.getInt("mnozstvi"),
                            rs.getString("nazev"),
                            rs.getString("popis_strucny"),
                            rs.getString("popis"),
                            rs.getString("obrazek"),
                            categoryRepository.getById(rs.getLong("kategorieid"))
                    );
                    return newProduct;
                });
        return products;
    }

    public Product getProduct(long productId) {
        Product product = jdbcTemplate.queryForObject(
                "select produktid, cena, mnozstvi, nazev, popis, popis_strucny, kategorieid, obrazek from produkty where produktid = ?",
                (rs, rowNum) -> {
                    Product newProduct = new Product(
                            rs.getLong("produktid"),
                            rs.getDouble("cena"),
                            rs.getInt("mnozstvi"),
                            rs.getString("nazev"),
                            rs.getString("popis_strucny"),
                            rs.getString("popis"),
                            rs.getString("obrazek"),
                            categoryRepository.getById(rs.getLong("kategorieid"))
                    );
                    return newProduct;
                }, productId);
        return product;
    }

    public void update(Product product) {
        jdbcTemplate.update(
                //cena, mnozstvi, nazev, popis, popis_strucny, kategorieid
                "update produkty set cena = ?, mnozstvi = ?, nazev = ?, popis = ?, popis_strucny = ?, kategorieid = ?" +
                        " where produktid = ?",
                product.getPrice(),
                product.getQuantity(),
                product.getName(),
                product.getDescription(),
                product.getDescriptionShort(),
                product.getCategory().getId(),
                product.getId());
    }

    public void save(Product product) {
        jdbcTemplate.update("insert into produkty (cena, mnozstvi, nazev, popis, popis_strucny, kategorieid, zamestnanecId)" +
                "values (?,?,?,?,?,?, 1)",
                product.getPrice(),
                product.getQuantity(),
                product.getName(),
                product.getDescription(),
                product.getDescriptionShort(),
                product.getCategory().getId());
    }

    public void delete(Product product) {
        jdbcTemplate.update("delete from produkty where produktid = ?",
                product.getId());
    }

    public List<Product> matchByName(String name) {
        List<Product> products = jdbcTemplate.query(
                "select produktid, cena, mnozstvi, nazev, popis, popis_strucny, kategorieid, obrazek from produkty " +
                        "where nazev like '%test%'",
                (rs, rowNum) -> {
                    Product newProduct = new Product(
                            rs.getLong("produktid"),
                            rs.getDouble("cena"),
                            rs.getInt("mnozstvi"),
                            rs.getString("nazev"),
                            rs.getString("popis_strucny"),
                            rs.getString("popis"),
                            rs.getString("obrazek"),
                            categoryRepository.getById(rs.getLong("kategorieid"))
                    );
                    return newProduct;
                }
                );
        return products;
    }
}
