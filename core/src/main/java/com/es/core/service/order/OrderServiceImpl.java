package com.es.core.service.order;

import com.es.core.dao.order.OrderDao;
import com.es.core.dao.order.OrderItemDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.stock.Stock;
import com.es.core.service.cart.CartRecalculationService;
import com.es.core.service.cart.CartService;
import com.es.core.service.stock.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private StockService stockService;
    @Resource
    private CartService cartService;
    @Resource
    private CartRecalculationService cartRecalculationService;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        setOrderFields(order, cart);

        return order;
    }

    @Override
    public void placeOrder(Order order) throws OutOfStockException {
        List<CartItem> outOfStockCartItems = cartService.removeOutOfStockCartItems();

        if (!outOfStockCartItems.isEmpty()) {
            setOrderFields(order, cartService.getCart());
            String outOfStockModels = outOfStockCartItems.stream()
                    .map(cartItem -> cartItem.getPhone().getModel())
                    .collect(Collectors.joining(", "));
            String errorMessage = "There are some products out of stock: " + outOfStockModels +
                    ". It will be removed from your cart.";

            throw new OutOfStockException(errorMessage);
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Stock stock = stockService.getStock(orderItem.getPhone().getId()).orElseThrow(StockNotFoundException::new);
            stock.setStock(stock.getStock() - orderItem.getQuantity().intValue());
            stockService.save(stock);
        }

        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        orderItemDao.insertOrderItems(order);
        cartService.clear();
    }

    @Override
    public Order getBySecureId(String secureId) throws OrderNotFoundException {
        return orderDao.getBySecureId(secureId).orElseThrow(OrderNotFoundException::new);
    }

    private void setOrderFields(Order order, Cart cart) {
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.NEW);
        }

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> new OrderItem(cartItem.getPhone(), order, cartItem.getQuantity()))
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);

        BigDecimal subtotal = cart.getTotalPrice();
        BigDecimal deliveryPrice = cartRecalculationService.getDeliveryPrice();
        order.setSubtotal(subtotal);
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(subtotal.add(deliveryPrice));
    }
}
