package cz.example.kotoucovnaeshop.util;

import cz.example.kotoucovnaeshop.model.OrderState;

public abstract class OrderStates {
    public static final OrderState UNCONFIRMED = new OrderState()
            .setId(1)
            .setName("Vytvořená");
    public static final OrderState READY_TO_SHIP = new OrderState()
            .setId(2)
            .setName("Potvrzená");
    public static final OrderState SHIPPED = new OrderState()
            .setId(3)
            .setName("Odeslána");
    public static final OrderState DELIVERED = new OrderState()
            .setId(4)
            .setName("Doručena");
}
