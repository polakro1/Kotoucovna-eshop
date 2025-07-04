package cz.example.kotoucovnaeshop.model;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private long id;
    private LocalDateTime orderDate;
    private LocalDate shippingDate;
    @Valid
    private Adress shippingAdress;
    @Valid
    private Adress billingAdress;
    private Client client;
    private Employee employee;
    private List<OrderItem> orderItems;

    private PaymentType paymentType;
    private ShippingType shippingType;
    private OrderState orderState;
    @Valid
    private Adressee adressee;


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

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
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

    public Adressee getAdressee() {
        return adressee;
    }

    public void setAdressee(Adressee adressee) {
        this.adressee = adressee;
    }

    public double getSumPrice() {
        double sum = 0;
        for (OrderItem orderItem :
                orderItems) {
            sum += orderItem.getPrice() * orderItem.getQuantity();
        }

        sum += paymentType.getPrice();
        sum += shippingType.getPrice();

        return sum;
    }
}
