package com.es.core.service.order;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order) throws OutOfStockException;

    Order getById(Long id) throws OrderNotFoundException;

    Order getBySecureId(String secureId) throws OrderNotFoundException;

    List<Order> getOrders();

    void updateOrderStatus(Long id, OrderStatus orderStatus);
}
