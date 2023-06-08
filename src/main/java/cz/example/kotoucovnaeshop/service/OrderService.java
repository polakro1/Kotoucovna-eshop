package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.model.Employee;
import cz.example.kotoucovnaeshop.model.Order;
import cz.example.kotoucovnaeshop.model.OrderState;
import cz.example.kotoucovnaeshop.repository.impl.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepositoryImpl orderRepository;
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    AdminService adminService;

    public void saveOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        orderRepository.saveOrder(order);
        shoppingCartService.clearCart();
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order getOrderById(long id) {
        return orderRepository.getOrderById(id);
    }

    public void updateOrderState(Order order, OrderState orderState) {
        if (orderState.getId() == TypesAndStatesService.ORDER_CONFIRMED) {
            Employee employee = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            order.setEmployee(employee);
            orderRepository.setConfirmEmployee(order);
        }
        orderRepository.updateOrderState(order, orderState);
    }

    public List<Order> getOrdersByClient(Client client) {
        return orderRepository.getOrdersByClient(client);
    }

    public List<Order> getOrdersByState(OrderState orderState) {
        return orderRepository.getOrdersByState(orderState);
    }

    public void updateShippingDate(Order order, LocalDate shippingDate) {
        orderRepository.updateShippingDate(order, shippingDate);
    }

    public long getNumberOfUnconfirmedOrders() {
        return orderRepository.getNumberOfUncofirmedOrders();
    }

    public long getNumberOfReadyToShipOrders() {
        return orderRepository.getNumberOfReadyToShipOrders();
    }

    public long getNumberShippedOrders() {
        return orderRepository.getNumberShippedOrders();
    }

    public long getNumberOfDeliveredOrders() {
        return orderRepository.getNumberOfDelivedOrders();
    }

    public long getNumberOfOrders() {
        return orderRepository.getNumberOfOrders();
    }
}

