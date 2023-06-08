package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Employee;
import cz.example.kotoucovnaeshop.model.Image;
import cz.example.kotoucovnaeshop.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository {
    public static final String PRICE_DESC = "cena desc";
    public static final String PRICE_ASC = "cena asc";
    public static final String NAME_DESC = "pnazev desc";
    public static final String NAME_ASC = "pnazev asc";
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall procAddProduct;
    private SimpleJdbcCall procDeleteProduct;

    private final String sqlProduct = "select produktid, cena, mnozstvi, pnazev, popis, popis_strucny, " +
            "knazev, kategorieid, nadkategorie, " +
            "obrazekid, onazev, cesta, pripona, " +
            "email, jmeno, prijmeni " +
            "from vsechny_produkty ";

    private final RowMapper<Product> productRowMapper = (rs, row) -> {
        Category category = new Category();
        category.setId(rs.getLong("kategorieid"));
        category.setName(rs.getString("knazev"));
        category.setSuperCategory(rs.getLong("nadkategorie"));

        Image image = new Image();
        image.setId(rs.getLong("obrazekid"));
        image.setName(rs.getString("onazev"));
        image.setPath(rs.getString("cesta"));
        image.setExtension(rs.getString("pripona"));

        Employee employee = new Employee();
        employee.setEmail(rs.getString("email"));
        employee.setName(rs.getString("jmeno"));
        employee.setSurname(rs.getString("prijmeni"));

        Product product = new Product();
        product.setId(rs.getLong("produktid"));
        product.setPrice(rs.getInt("cena"));
        product.setQuantity(rs.getInt("mnozstvi"));
        product.setName(rs.getString("pnazev"));
        product.setDescription(rs.getString("popis"));
        product.setDescriptionShort(rs.getString("popis_strucny"));
        product.setCategory(category);
        product.setImage(image);
        product.setEmployee(employee);

        return product;
    };

    @PostConstruct
    private void initProcAddProduct() {
        procAddProduct = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("pridej_produkt");
    }

    @PostConstruct
    private void initProcDeleteProduct() {
        procDeleteProduct = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("odstran_produkt");
    }

    public List<Product> findAllByCategory(Category category, String orderBy) {
        String sql = sqlProduct +
                "where kategorieid = ? or " +
                "kategorieid in (select kategorieid from kategorie where nadkategorie = ?) " +
                "order by " + orderBy;

        List<Product> products = jdbcTemplate.query(
                sql, productRowMapper, category.getId(), category.getId());
        return products;
    }

    public List<Product> findAll() {
        List<Product> products = jdbcTemplate.query(
                sqlProduct,
                productRowMapper);
        return products;
    }

    public Product getProductById(long productId) {
        Product product = jdbcTemplate.queryForObject(
                sqlProduct +
                        "where produktid = ?",
                productRowMapper, productId);
        return product;
    }

    public Product getProductByName(String name) {
        Product product = jdbcTemplate.queryForObject(
                sqlProduct +
                        "where nlssort(pnazev, 'NLS_SORT=BINARY_AI') = NLSSORT(?, 'NLS_SORT=BINARY_AI')",
                productRowMapper, name
        );

        return product;
    }

    public void edit(Product product) {
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

    public void changeImage(Image newImage, Product product) {
        jdbcTemplate.update(
                "update obrazky set nazev = ?, cesta = ?, pripona = ? " +
                        "where obrazekid = ?",
                newImage.getName(),
                newImage.getPath(),
                newImage.getExtension(),
                product.getImage().getId()
        );
    }

    public long save(Product product) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_nazev", product.getName())
                .addValue("in_cena", product.getPrice())
                .addValue("in_mnozstvi", product.getQuantity())
                .addValue("in_popis_strucny", product.getDescriptionShort())
                .addValue("in_popis", product.getDescription())
                .addValue("in_kategorieid", product.getCategory().getId())
                .addValue("in_zamestnanecid", product.getEmployee().getId())
                .addValue("in_obrazek_nazev", product.getImage().getName())
                .addValue("in_obrazek_pripona", product.getImage().getExtension())
                .addValue("in_obrazek_cesta", product.getImage().getPath());

        Map out = procAddProduct.execute(in);

        BigDecimal produktId = (BigDecimal) out.get("OUT_PRODUKTID");

        return produktId.longValue();
        /*
        jdbcTemplate.update("insert into produkty (cena, mnozstvi, nazev, popis, popis_strucny, kategorieid, zamestnanecId)" +
                "values (?,?,?,?,?,?, 1)",
                product.getPrice(),
                product.getQuantity(),
                product.getName(),
                product.getDescription(),
                product.getDescriptionShort(),
                product.getCategory().getId());

         */
    }

    public void delete(Product product) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_produktid", product.getId());

        procDeleteProduct.execute(in);
    }

    public List<Product> matchByName(String name, String orderBy) {
        name = "%" + name + "%";
        List<Product> products = jdbcTemplate.query(
                sqlProduct +
                        "where upper(pnazev) like upper(?) " +
                        "order by " + orderBy,
                productRowMapper, name
        );
        return products;
    }

    public List<Product> getByNameAndCategory(String name, Category category, String orderBy) {
        name = "%" + name + "%";
        List<Product> products = jdbcTemplate.query(
                sqlProduct +
                        "where upper(pnazev) like upper(?) and (kategorieid = ? or " +
                        "kategorieid in (select kategorieid from kategorie where nadkategorie = ?)) " +
                        "order by " + orderBy,
                productRowMapper, name, category.getId(), category.getId()
        );
        return products;
    }

    public long getNumberOfProducts() {
        return jdbcTemplate.queryForObject(
                "select produktu_celkem from pocty_produktu", Long.class
        );
    }

    public long getNumberOfLowQuantityProducts() {
        return jdbcTemplate.queryForObject(
                "select produktu_s_nizkym_mnozstvim from pocty_produktu", Long.class
        );
    }

    public long getNumberOfOutOfStockProducts() {
        return jdbcTemplate.queryForObject(
                "select produktu_neskladem from pocty_produktu", Long.class
        );
    }
}
