package cz.example.kotoucovnaeshop.service;

import cz.example.kotoucovnaeshop.model.*;
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
    private UserService userService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private TypesAndStatesService typesAndStatesService;

    @Autowired AdminService adminService;
    public void saveOrder(Order order) {
        if (order.getClient().getId() == null) {
            order.getClient().setId(userService.saveUser(order.getClient()));
        }
        Long billingAdressId = null;
        if (order.getBillingAdress() != null) {
            billingAdressId = adressService.saveAdress(order.getBillingAdress());
        }
        order.getBillingAdress().setId(billingAdressId);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderState(typesAndStatesService.getOrderState(1));
        orderRepository.saveOrder(order);
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
    public List<Order> getUnconfirmedOrders() {
        return orderRepository.getUnconfirmedOrders();
    }

    public void updateShippingDate(Order order, LocalDate shippingDate) {
        orderRepository.updateShippingDate(order, shippingDate);
    }
}

