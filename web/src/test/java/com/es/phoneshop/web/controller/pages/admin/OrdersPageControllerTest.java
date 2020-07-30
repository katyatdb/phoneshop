package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.service.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class OrdersPageControllerTest {
    private static final String ordersUrl = "/admin/orders";
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private Order order1;
    @Mock
    private Order order2;
    @Spy
    private ArrayList<Order> orders;

    @InjectMocks
    private OrdersPageController ordersPageController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersPageController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/admin/orders.jsp"))
                .build();

        orders.addAll(Arrays.asList(order1, order2));
    }

    @Test
    public void testGetOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(orders);

        mockMvc.perform(get(ordersUrl))
                .andExpect(model().attribute("orders", orders))
                .andExpect(view().name("admin/orders"));
    }
}
