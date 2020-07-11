package com.es.core.service.order;

import com.es.core.dao.order.OrderDao;
import com.es.core.dao.order.OrderItemDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import com.es.core.service.cart.CartRecalculationService;
import com.es.core.service.cart.CartService;
import com.es.core.service.stock.StockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderItemDao orderItemDao;
    @Mock
    private CartService cartService;
    @Mock
    private CartRecalculationService cartRecalculationService;
    @Mock
    private Cart cart;
    @Mock
    private StockService stockService;
    @Mock
    private Order order;
    @Mock
    private OrderItem orderItem1;
    @Mock
    private OrderItem orderItem2;
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Spy
    private ArrayList<OrderItem> orderItems;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Before
    public void init() {
        when(orderItem1.getPhone()).thenReturn(phone1);
        when(orderItem2.getPhone()).thenReturn(phone2);
        when(orderItem1.getQuantity()).thenReturn(2L);
        when(orderItem2.getQuantity()).thenReturn(4L);
        when(phone1.getId()).thenReturn(1L);
        when(phone2.getId()).thenReturn(2L);

        orderItems.addAll(Arrays.asList(orderItem1, orderItem2));
        when(order.getOrderItems()).thenReturn(orderItems);
        when(order.getStatus()).thenReturn(OrderStatus.NEW);

        CartItem cartItem1 = mock(CartItem.class);
        CartItem cartItem2 = mock(CartItem.class);
        List<CartItem> cartItems = new ArrayList<>();
        List<CartItem> cartItemsSpy = spy(cartItems);
        cartItemsSpy.addAll(Arrays.asList(cartItem1, cartItem2));

        when(cart.getCartItems()).thenReturn(cartItemsSpy);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(700));
        when(cartRecalculationService.getDeliveryPrice()).thenReturn(new BigDecimal(10));
        when(cartService.getCart()).thenReturn(cart);
    }

    @Test
    public void testCreateOrder() {
        Order order = orderService.createOrder(cart);

        assertEquals(2, order.getOrderItems().size());
        assertEquals(new BigDecimal(710), order.getTotalPrice());
    }

    @Test
    public void testPlaceOrder() {
        Stock stock1 = new Stock(phone1, 5, 1);
        Stock stock2 = new Stock(phone2, 10, 1);
        when(stockService.getStock(1L)).thenReturn(Optional.of(stock1));
        when(stockService.getStock(2L)).thenReturn(Optional.of(stock2));
        when(cartService.removeOutOfStockCartItems()).thenReturn(Collections.emptyList());

        orderService.placeOrder(order);

        verify(stockService).save(stock1);
        verify(stockService).save(stock2);
        verify(orderDao).save(order);
        assertEquals(3, stock1.getStock().intValue());
        assertEquals(6, stock2.getStock().intValue());
    }

    @Test(expected = OutOfStockException.class)
    public void testPlaceOrderOutOfStock() {
        CartItem cartItemOutOfStock = mock(CartItem.class);
        when(cartItemOutOfStock.getPhone()).thenReturn(phone1);
        when(cartService.removeOutOfStockCartItems())
                .thenReturn(Collections.singletonList(cartItemOutOfStock));

        orderService.placeOrder(order);
    }

    @Test
    public void testGetOrderBySecureId() {
        when(orderDao.getBySecureId("order1")).thenReturn(Optional.of(order));

        assertEquals(order, orderService.getBySecureId("order1"));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetNullOrderBySecureId() {
        orderService.getBySecureId("order1");
    }
}
