package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Order;
import cz.example.kotoucovnaeshop.model.OrderState;
import cz.example.kotoucovnaeshop.service.OrderService;
import cz.example.kotoucovnaeshop.service.TypesAndStatesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("admin/orders")
    public String manageOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);

        return "admin/orderList";
    }
    @GetMapping("/cart/create-order")
    public String createOrder(@SessionAttribute Order order) {
        orderService.saveOrder(order);

        return "redirect:/cart";
    }

    @GetMapping("admin/order")
    public String orderDetail(@RequestParam("order") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        System.out.println(orderId);
        return "admin/orderDetail";
    }

    @PostMapping("/admin/order/confirm")
    public String confirm(@ModelAttribute("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        Order order = new Order();
        order.setId(orderId);
        OrderState orderState = new OrderState();
        orderState.setId(TypesAndStatesService.ORDER_CONFIRMED);
        orderService.updateOrderState(order, orderState);
        System.out.println("daffdfa");

        redirectAttributes.addAttribute("order", orderId);

        return "redirect:/admin/order";
    }

    @PostMapping("/admin/order/confirm-shipping")
    public String confirmShipping(@ModelAttribute("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        Order order = new Order();
        order.setId(orderId);
        OrderState orderState = new OrderState();
        orderState.setId(TypesAndStatesService.ORDER_SHIPPED);
        orderService.updateOrderState(order, orderState);
        System.out.println("daffdfa");

        redirectAttributes.addAttribute("order", orderId);

        return "redirect:/admin/order";
    }

    @PostMapping("/admin/order/confirm-delivery")
    public String confirmDelivery(@ModelAttribute("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        Order order = new Order();
        order.setId(orderId);
        OrderState orderState = new OrderState();
        orderState.setId(TypesAndStatesService.ORDER_DELIVERED);
        orderService.updateOrderState(order, orderState);
        System.out.println("daffdfa");

        redirectAttributes.addAttribute("order", orderId);

        return "redirect:/admin/order";
    }

}
