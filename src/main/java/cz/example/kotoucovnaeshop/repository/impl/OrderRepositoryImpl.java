package cz.example.kotoucovnaeshop.repository.impl;

import cz.example.kotoucovnaeshop.model.*;
import cz.example.kotoucovnaeshop.service.AdressService;
import cz.example.kotoucovnaeshop.service.ProductService;
import cz.example.kotoucovnaeshop.service.TypesAndStatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductService productService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private TypesAndStatesService typesAndStatesService;
    @Autowired
    private CustomerRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    private SimpleJdbcCall procAddOrder;

    private final ResultSetExtractor<List<Order>> orderResultSetExtractor = rs -> {
        Map<Long, Order> orders = new HashMap<>();

        while (rs.next()) {
            long objednavkaid = rs.getLong("objednavkaid");
            if (!orders.containsKey(objednavkaid)) {
                Order order = new Order();

                order.setId(rs.getLong("objednavkaid"));

                Date shippingDate = rs.getDate("datum_dodani");
                if (shippingDate != null) {
                    order.setShippingDate(shippingDate.toLocalDate());
                }
                order.setOrderDate(rs.getTimestamp("datum_objednani").toLocalDateTime());

                //shippingAdress
                Adress shippingAdress = new Adress();
                shippingAdress.setId(rs.getLong("adresa_dodani"));
                shippingAdress.setBuildingNumber(rs.getString("ad_cislo_popisne"));
                shippingAdress.setStreet(rs.getString("ad_ulice"));
                shippingAdress.setCity(rs.getString("ad_mesto"));
                shippingAdress.setPostalCode(rs.getString("ad_psc"));
                shippingAdress.setCountry(rs.getString("ad_zeme"));
                order.setShippingAdress(shippingAdress);

                //billingAdress
                Adress billingAdress = new Adress();
                billingAdress.setId(rs.getLong("adresa_fakturace"));
                billingAdress.setBuildingNumber(rs.getString("af_cislo_popisne"));
                billingAdress.setStreet(rs.getString("af_ulice"));
                billingAdress.setCity(rs.getString("af_mesto"));
                billingAdress.setPostalCode(rs.getString("af_psc"));
                billingAdress.setCountry(rs.getString("af_zeme"));
                order.setBillingAdress(billingAdress);

                //PaymentType
                PaymentType paymentType = new PaymentType();
                paymentType.setId(rs.getLong("typy_platbyid"));
                paymentType.setName(rs.getString("tp_nazev"));
                paymentType.setPrice(rs.getInt("tp_cena"));
                order.setPaymentType(paymentType);

                //ShippingType
                ShippingType shippingType = new ShippingType();
                shippingType.setId(rs.getLong("typ_dopravyid"));
                shippingType.setName(rs.getString("td_nazev"));
                shippingType.setPrice(rs.getInt("td_cena"));
                order.setShippingType(shippingType);


                //OrderState
                OrderState orderState = new OrderState();
                orderState.setId(rs.getLong("stav_objednavkyid"));
                orderState.setName(rs.getString("so_nazev"));
                order.setOrderState(orderState);

                //Adresee
                Adressee adressee = new Adressee();
                adressee.setId(rs.getLong("adresatid"));
                adressee.setEmail(rs.getString("a_email"));
                adressee.setName(rs.getString("a_jmeno"));
                adressee.setSurname(rs.getString("a_prijmeni"));
                adressee.setTel(rs.getString("a_tel"));
                order.setAdressee(adressee);

                //Customer
                if (rs.getString("zakaznikid") != null) {
                    Client client = new Client();
                    client.setId(rs.getLong("zakaznikid"));
                    client.setEmail(rs.getString("z_email"));
                    client.setName(rs.getString("z_jmeno"));
                    client.setSurname(rs.getString("z_prijmeni"));
                    client.setUsername(rs.getString("z_prihlasovaci_jmeno"));
                    client.setTel(rs.getString("z_tel"));
                    order.setClient(client);

                    //CustomerAdress
                    if (rs.getString("z_adresaid") != null) {
                        Adress customerAdress = new Adress();
                        customerAdress.setId(rs.getLong("z_adresaid"));
                        customerAdress.setBuildingNumber(rs.getString("az_cislo_popisne"));
                        customerAdress.setStreet(rs.getString("az_ulice"));
                        customerAdress.setCity(rs.getString("az_mesto"));
                        customerAdress.setPostalCode(rs.getString("az_psc"));
                        customerAdress.setCountry(rs.getString("az_zeme"));
                        client.setAdress(customerAdress);
                    }
                }

                //Employee
                if (rs.getString("zamestnanecid") != null) {
                    Employee employee = new Employee();
                    employee.setId(rs.getLong("zamestnanecid"));
                    employee.setEmail(rs.getString("zm_email"));
                    employee.setName(rs.getString("zm_jmeno"));
                    employee.setSurname(rs.getString("zm_prijmeni"));
                    employee.setUsername(rs.getString("zm_prihlasovaci_jmeno"));
                    order.setEmployee(employee);
                }

                orders.put(order.getId(), order);
            }

            //OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getLong("polozky_objednavkyid"));
            orderItem.setQuantity(rs.getInt("po_mnozstvi"));
            orderItem.setPrice(rs.getDouble("po_cena"));
            Order order = orders.get(objednavkaid);
            if (order.getOrderItems() == null) {
                order.setOrderItems(new ArrayList<>());
            }
            order.getOrderItems().add(orderItem);

            //Product
            Product product = new Product();
            product.setId(rs.getLong("produktid"));
            product.setName(rs.getString("p_nazev"));
            product.setQuantity(rs.getInt("p_mnozstvi"));
            product.setPrice(rs.getInt("p_cena"));
            product.setDescription(rs.getString("p_popis"));
            product.setDescriptionShort(rs.getString("popis_strucny"));
            orderItem.setProduct(product);

            //Image
            Image image = new Image();
            image.setId(rs.getLong("obrazekid"));
            image.setName(rs.getString("ob_nazev"));
            image.setExtension(rs.getString("ob_pripona"));
            image.setPath(rs.getString("ob_cesta"));
            product.setImage(image);

            //Category
            Category category = new Category();
            category.setId(rs.getLong("kategorieid"));
            category.setName(rs.getString("k_nazev"));
            category.setSuperCategory(rs.getLong("nadkategorie"));
            product.setCategory(category);

        }

        return new ArrayList<Order>(orders.values());
    };
    private final RowMapper<Order> orderRowMapper1 = (rs, rowNum) -> {
        Order order = new Order();

        order.setId(rs.getLong("objednavkaid"));

        Date shippingDate = rs.getDate("datum_dodani");
        if (shippingDate != null) {
            order.setShippingDate(shippingDate.toLocalDate());
        }
        order.setOrderDate(rs.getTimestamp("datum_objednani").toLocalDateTime());

        //shippingAdress
        Adress shippingAdress = new Adress();
        shippingAdress.setId(rs.getLong("adresa_dodani"));
        shippingAdress.setBuildingNumber(rs.getString("ad_cislo_popisne"));
        shippingAdress.setStreet(rs.getString("ad_ulice"));
        shippingAdress.setCity(rs.getString("ad_mesto"));
        shippingAdress.setPostalCode(rs.getString("ad_psc"));
        shippingAdress.setCountry(rs.getString("ad_zeme"));
        order.setShippingAdress(shippingAdress);

        //billingAdress
        Adress billingAdress = new Adress();
        billingAdress.setId(rs.getLong("adresa_fakturace"));
        billingAdress.setBuildingNumber(rs.getString("af_cislo_popisne"));
        billingAdress.setStreet(rs.getString("af_ulice"));
        billingAdress.setCity(rs.getString("af_mesto"));
        billingAdress.setPostalCode(rs.getString("af_psc"));
        billingAdress.setCountry(rs.getString("af_zeme"));
        order.setBillingAdress(billingAdress);

        //PaymentType
        PaymentType paymentType = new PaymentType();
        paymentType.setId(rs.getLong("typy_platbyid"));
        paymentType.setName(rs.getString("tp_nazev"));
        paymentType.setPrice(rs.getInt("tp_cena"));
        order.setPaymentType(paymentType);

        //ShippingType
        ShippingType shippingType = new ShippingType();
        shippingType.setId(rs.getLong("typ_dopravyid"));
        shippingType.setName(rs.getString("td_nazev"));
        shippingType.setPrice(rs.getInt("td_cena"));
        order.setShippingType(shippingType);


        //OrderState
        OrderState orderState = new OrderState();
        orderState.setId(rs.getLong("stav_objednavkyid"));
        orderState.setName(rs.getString("so_nazev"));
        order.setOrderState(orderState);

        //Adresee
        Adressee adressee = new Adressee();
        adressee.setId(rs.getLong("adresatid"));
        adressee.setEmail(rs.getString("a_email"));
        adressee.setName(rs.getString("a_jmeno"));
        adressee.setSurname(rs.getString("a_prijmeni"));
        adressee.setTel(rs.getString("a_tel"));
        order.setAdressee(adressee);

        //Customer
        if (rs.getString("zakaznikid") != null) {
            Client client = new Client();
            client.setId(rs.getLong("zakaznikid"));
            client.setEmail(rs.getString("z_email"));
            client.setName(rs.getString("z_jmeno"));
            client.setSurname(rs.getString("z_prijmeni"));
            client.setUsername(rs.getString("z_prihlasovaci_jmeno"));
            client.setTel(rs.getString("z_tel"));
            order.setClient(client);

            //CustomerAdress
            if (rs.getString("z_adresaid") != null) {
                Adress customerAdress = new Adress();
                customerAdress.setId(rs.getLong("z_adresaid"));
                customerAdress.setBuildingNumber(rs.getString("az_cislo_popisne"));
                customerAdress.setStreet(rs.getString("az_ulice"));
                customerAdress.setCity(rs.getString("az_mesto"));
                customerAdress.setPostalCode(rs.getString("az_psc"));
                customerAdress.setCountry(rs.getString("az_zeme"));
                client.setAdress(customerAdress);
            }
        }

        //Employee
        if (rs.getString("zamestnanecid") != null) {
            Employee employee = new Employee();
            employee.setId(rs.getLong("zamestnanecid"));
            employee.setEmail(rs.getString("zm_email"));
            employee.setName(rs.getString("zm_jmeno"));
            employee.setSurname(rs.getString("zm_prijmeni"));
            employee.setUsername(rs.getString("zm_prihlasovaci_jmeno"));
            order.setEmployee(employee);
        }

        List<OrderItem> items = new ArrayList<>();
        do {

            //OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getInt("polozky_objednavkyid"));
            orderItem.setQuantity(rs.getInt("po_mnozstvi"));
            orderItem.setPrice(rs.getDouble("po_cena"));
            items.add(orderItem);

            //Product
            Product product = new Product();
            product.setId(rs.getLong("produktid"));
            product.setName(rs.getString("p_nazev"));
            product.setQuantity(rs.getInt("p_mnozstvi"));
            product.setPrice(rs.getInt("p_cena"));
            product.setDescription(rs.getString("p_popis"));
            product.setDescriptionShort(rs.getString("popis_strucny"));
            orderItem.setProduct(product);

            //Image
            Image image = new Image();
            image.setId(rs.getLong("obrazekid"));
            image.setName(rs.getString("ob_nazev"));
            image.setExtension(rs.getString("ob_pripona"));
            image.setPath(rs.getString("ob_cesta"));
            product.setImage(image);


        } while (rs.next() && rs.getInt("objednavkaid") == order.getId());

        order.setOrderItems(items);

        return order;

    };

    private final RowMapper<Order> orderRowMapper = (resultSet, rowNum) -> {
        Order order = new Order();
        order.setId(resultSet.getLong("objednavkaid"));
        Date shippingDate = resultSet.getDate("datum_dodani");
        if (shippingDate != null) {
            order.setShippingDate(shippingDate.toLocalDate());
        } else {
            order.setShippingDate(null);
        }
        order.setOrderDate(resultSet.getTimestamp("datum_objednani").toLocalDateTime());
        order.setShippingAdress(
                adressService.getAdress(resultSet.getLong("adresa_dodani"))
        );
        order.setBillingAdress(
                adressService.getAdress(resultSet.getLong("adresa_fakturace")));
        order.setPaymentType(typesAndStatesService.getPaymentType(resultSet.getLong("typy_platbyid")));
        order.setShippingType(typesAndStatesService.getShippingType(resultSet.getLong("typ_dopravyid")));
        order.setOrderState(typesAndStatesService.getOrderState(resultSet.getLong("stav_objednavkyid")));
        order.setAdressee(getAdresse(resultSet.getLong("adresatid")));
        if (resultSet.getString("zakaznikid") != null) {
            order.setClient(userRepository.findById(resultSet.getLong("zakaznikid")));
        } else {
            order.setClient(null);
        }
        if (resultSet.getString("zamestnanecid") != null) {
            order.setEmployee(adminRepository.getById(resultSet.getLong("zamestnanecid")));
        } else {
            order.setEmployee(null);
        }
        order.setOrderItems(getOrderItems(order));

        return order;
    };

    public List<Order> getAllOrders() {
        List<Order> orders = jdbcTemplate.query(
                "select * from vsechny_objednavky",
                orderResultSetExtractor
        );

        /*
        List<Order> orders = jdbcTemplate.query(
                "select objednavkaid, datum_dodani, datum_objednani, adresa_dodani,adresa_fakturace," +
                        "typy_platbyid, typ_dopravyid, stav_objednavkyid, zakaznikid, zamestnanecid, adresatid from objednavky " +
                        "order by stav_objednavkyid, datum_objednani" ,
                orderRowMapper);

         */
        return orders;
    }

    public void saveOrder(Order order) {
        /*
        Long billingAdressId = null;
        if (order.getBillingAdress() != null) {
            billingAdressId = adressService.saveAdress(order.getBillingAdress());
        }
        if (simpleJdbcInsert == null) {
            initializeSimpleJdbcInsert();
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("datum_dodani", order.getShippingDate());
        parameters.put("datum_objednani", LocalDateTime.now());
        parameters.put("adresa_dodani", adressService.saveAdress(order.getShippingAdress()));
        parameters.put("adresa_fakturace", billingAdressId);
        parameters.put("typ_dopravyid", order.getShippingType().getId());
        parameters.put("typy_platbyid", order.getPaymentType().getId());
        parameters.put("stav_objednavkyid", order.getOrderState().getId());
        parameters.put("zakaznikid", order.getClient().getId());
        order.setId(simpleJdbcInsert.executeAndReturnKey(parameters).longValue());
        saveOrderItems(order);

        return order.getId();

         */

        if (procAddOrder == null) {
            procAddOrder = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("vytvor_objednavku");
        }

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_zakaznikid", order.getClient().getId())

                .addValue("in_jmeno", order.getAdressee().getName())
                .addValue("in_prijmeni", order.getAdressee().getSurname())
                .addValue("in_email", order.getAdressee().getEmail())
                .addValue("in_tel", order.getAdressee().getTel())

                .addValue("in_datum_objednani", order.getOrderDate())
                .addValue("in_typ_dopravyid", order.getShippingType().getId())
                .addValue("in_typ_platbyid", order.getPaymentType().getId())

                .addValue("in_ulice_dodani", order.getShippingAdress().getStreet())
                .addValue("in_cislo_popisne_dodani", order.getShippingAdress().getBuildingNumber())
                .addValue("in_mesto_dodani", order.getShippingAdress().getCity())
                .addValue("in_psc_dodani", order.getShippingAdress().getPostalCode())
                .addValue("in_zeme_dodani", order.getShippingAdress().getCountry())

                .addValue("in_ulice_fakturace", order.getBillingAdress().getStreet())
                .addValue("in_cislo_popisne_fakturace", order.getBillingAdress().getBuildingNumber())
                .addValue("in_mesto_fakturace", order.getBillingAdress().getCity())
                .addValue("in_psc_fakturace", order.getBillingAdress().getPostalCode())
                .addValue("in_zeme_fakturace", order.getBillingAdress().getCountry());
        if (order.getClient() != null) {
            in.addValue("in_zakaznikid", order.getClient().getId());
        }

        Map out = procAddOrder.execute(in);

        if (order.getClient().getId() == null) {
            BigDecimal orderId = (BigDecimal) out.get("OUT_OBJEDNAVKAID");
            order.setId(orderId.longValue());
            saveOrderItems(order);
        }
    }

    private void saveOrderItems(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem :
                orderItems) {
            jdbcTemplate.update(
                    "insert into polozky_objednavky (cena, mnozstvi, objednavkyid, produktyid)" +
                            "values (?, ?, ?, ?)",
                    orderItem.getPrice(),
                    orderItem.getQuantity(),
                    order.getId(),
                    orderItem.getProduct().getId()
            );
        }
    }

    /*
        private void saveAdressee(Order order) {
            jdbcTemplate.update("update adresati")
        }

     */
    private List<OrderItem> getOrderItems(Order order) {
        List<OrderItem> orderItems = jdbcTemplate.query(
                "select polozky_objednavkyid, cena, mnozstvi, produktyId from polozky_objednavky " +
                        "where objednavkyid = ?",
                (rs, rowNum) -> {
                    OrderItem orderItem = new OrderItem(
                            rs.getLong("polozky_objednavkyid"),
                            productService.getProduct(rs.getLong("produktyid")),
                            rs.getInt("mnozstvi"),
                            rs.getDouble("cena")
                    );
                    return orderItem;
                }, order.getId());
        return orderItems;
    }

    public Order getOrderById(long id) {
        List<Order> result = jdbcTemplate.query(
                "select * from vsechny_objednavky " +
                        "where objednavkaid = ?",
                orderResultSetExtractor,
                id
        );

        if (result.size() != 1) {
            throw new IncorrectResultSizeDataAccessException(1, result.size());
        }

        return result.get(0);
    }

    public void updateOrderState(Order order, OrderState orderState) {
        jdbcTemplate.update("update objednavky set stav_objednavkyid = ? where objednavkaid = ?",
                orderState.getId(),
                order.getId());
    }

    public void setConfirmEmployee(Order order) {
        jdbcTemplate.update("update objednavky set zamestnanecid = ? where objednavkaid = ?",
                order.getEmployee().getId(),
                order.getId());
    }

    public List<Order> getOrdersByClient(Client client) {
        List<Order> orders = jdbcTemplate.query(
                "select * from vsechny_objednavky " +
                        "where zakaznikid = ?",
                orderResultSetExtractor,
                client.getId()
        );
        return orders;
    }


    public List<Order> getOrdersByState(OrderState orderState) {
        List<Order> orders = jdbcTemplate.query(
                "select * from vsechny_objednavky " +
                        "where stav_objednavkyid = ?",
                orderResultSetExtractor,
                orderState.getId());
        return orders;
    }

    public void updateShippingDate(Order order, LocalDate shippingDate) {
        jdbcTemplate.update("update objednavky set datum_dodani = ? where objednavkaid = ?",
                shippingDate,
                order.getId());
    }

    private Adressee getAdresse(long id) {
        Adressee adressee = jdbcTemplate.queryForObject(
                "select email, jmeno, prijmeni, tel from adresati " +
                        "where adresatid = ?",
                (rs, rowNum) -> {
                    Adressee newAdresse = new Adressee();
                    newAdresse.setEmail(rs.getString("email"));
                    newAdresse.setName(rs.getString("jmeno"));
                    newAdresse.setSurname(rs.getString("prijmeni"));
                    newAdresse.setTel(rs.getString("tel"));

                    return newAdresse;
                }
                , id
        );
        return adressee;
    }

    public long getNumberOfUncofirmedOrders() {
        long number = jdbcTemplate.queryForObject(
                "select nepotvrzene_objednavky from pocty_objednavek",
                Long.class);

        return number;
    }

    public long getNumberOfReadyToShipOrders() {
        long number = jdbcTemplate.queryForObject(
                "select objednavky_k_odeslani from pocty_objednavek",
                Long.class);

        return number;
    }

    public long getNumberShippedOrders() {
        long number = jdbcTemplate.queryForObject(
                "select odeslane_objednavky from pocty_objednavek",
                Long.class);

        return number;
    }

    public long getNumberOfDelivedOrders() {
        long number = jdbcTemplate.queryForObject(
                "select dorucene_objednavky from pocty_objednavek",
                Long.class);

        return number;
    }

    public long getNumberOfOrders() {
        long number = jdbcTemplate.queryForObject(
                "select objednavek_celkem from pocty_objednavek",
                Long.class);

        return number;
    }

}
