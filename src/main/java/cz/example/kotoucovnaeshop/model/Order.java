package cz.example.kotoucovnaeshop.model;

import java.util.Date;
import java.util.List;

public class Order {
    private long id;
    private Date orderDate;
    private Date shippingDate;
    private Adress shippingAdress;
    private Adress billingAdress;
    private Client client;
    private Employee employee;
    private List<OrderItem> orderItems;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Adress getShippingAdress() {
        return shippingAdress;
    }

    public void setShippingAdress(Adress shippingAdress) {
        this.shippingAdress = shippingAdress;
    }

    public Adress getBillingAdress() {
        return billingAdress;
    }

    public void setBillingAdress(Adress billingAdress) {
        this.billingAdress = billingAdress;
    }

    public Client getKlient() {
        return client;
    }

    public void setKlient(Client client) {
        this.client = client;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
