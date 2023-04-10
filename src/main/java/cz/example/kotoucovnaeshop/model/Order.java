package cz.example.kotoucovnaeshop.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Order {
    private long id;
    private LocalDateTime orderDate;
    private Date shippingDate;
    private Adress shippingAdress;
    private Adress billingAdress;
    private Client client;
    private Employee employee;
    private List<OrderItem> orderItems;

    private PaymentType paymentType;
    private ShippingType shippingType;
    private OrderState orderState;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
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

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public ShippingType getShippingType() {
        return shippingType;
    }

    public void setShippingType(ShippingType shippingType) {
        this.shippingType = shippingType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
