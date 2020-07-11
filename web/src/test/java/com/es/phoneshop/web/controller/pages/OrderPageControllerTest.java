package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.service.cart.CartService;
import com.es.core.service.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class OrderPageControllerTest {
    private static final String orderUrl = "/order";
    private static final String userDataFormJson = "\"firstName\": \"name\", \"lastName\": \"surname\", " +
            "\"address\": \"address\", \"phone\": \"12345\"";
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private Order order;
    @InjectMocks
    private OrderPageController orderPageController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderPageController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/order.jsp"))
                .build();

        when(cartService.getCart()).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);
    }

    @Test
    public void testGetOrder() throws Exception {
        mockMvc.perform(get(orderUrl))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("userDataForm"))
                .andExpect(view().name("order"));
    }

    @Test
    public void testPlaceOrder() throws Exception {
        OrderItem orderItem = mock(OrderItem.class);
        when(order.getOrderItems()).thenReturn(Collections.singletonList(orderItem));
        when(order.getSecureId()).thenReturn("123");

        mockMvc.perform(post(orderUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDataFormJson))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("userDataForm"))
                .andExpect(view().name("redirect:/orderOverview/123"));
    }

    @Test
    public void testPlaceEmptyOrder() throws Exception {
        mockMvc.perform(post(orderUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDataFormJson))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("userDataForm"))
                .andExpect(view().name("order"));
    }

    @Test
    public void testPlaceOrderOutOfStock() throws Exception {
        OrderItem orderItem = mock(OrderItem.class);
        when(order.getOrderItems()).thenReturn(Collections.singletonList(orderItem));

        String errorMessage = "out of stock";
        doThrow(new OutOfStockException(errorMessage)).when(orderService).placeOrder(order);

        mockMvc.perform(post(orderUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDataFormJson))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("userDataForm"))
                .andExpect(model().attribute("outOfStockMessage", errorMessage))
                .andExpect(view().name("order"));
    }
}
