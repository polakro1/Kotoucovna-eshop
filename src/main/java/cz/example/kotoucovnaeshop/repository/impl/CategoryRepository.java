package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Category> getAllCategories() {
        List<Category> categories = jdbcTemplate.query(
                "select kategorieid, nazev, nadkategorie from kategorie",
                (rs, rowNum) -> {
                    Category category = new Category(
                            rs.getLong("kategorieid"),
                            rs.getString("nazev"),
                            rs.getLong("nadkategorie")
                    );
                    return category;
                });
        return categories;
    }


    public Category getById(long id) {
        Category category = jdbcTemplate.queryForObject(
                "select kategorieid, nazev, nadkategorie from kategorie where kategorieid = ?",
                (rs, rowNum) -> {
                    Category newCategory = new Category(
                            rs.getLong("kategorieid"),
                            rs.getString("nazev"),
                            rs.getLong("nadkategorie")
                    );
                    return newCategory;
                }, id
        );
        return category;
    }

    public Category getByName(String name) {
        Category category = jdbcTemplate.queryForObject(
                "select kategorieid, nazev, nadkategorie from kategorie " +
                        "where nlssort(nazev, 'NLS_SORT=BINARY_AI') = NLSSORT(?, 'NLS_SORT=BINARY_AI')",
                (rs, rowNum) -> {
                    Category newCategory = new Category(
                            rs.getLong("kategorieid"),
                            rs.getString("nazev"),
                            rs.getLong("nadkategorie")
                    );
                    return newCategory;
                }, name
        );
        return category;
    }

    public List<Category> getSubcategories(Category category) {
        List<Category> subcategories = jdbcTemplate.query(
                "select kategorieid, nazev, nadkategorie from kategorie where nadkategorie = ?",
                (rs, rowNum) -> {
                    Category subcategory = new Category(
                            rs.getLong("kategorieid"),
                            rs.getString("nazev"),
                            rs.getLong("nadkategorie")
                    );
                    return subcategory;
                }, category.getId()
        );
        return subcategories;
    }
}
