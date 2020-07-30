package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.service.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class OrderDetailsPageControllerTest {
    private static final String orderUrl = "/admin/orders/{id}";
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private Order order;
    @InjectMocks
    private OrderDetailsPageController orderDetailsPageController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderDetailsPageController)
                .setSingleView(new InternalResourceView("WEB-INF/pages/admin/orderDetails.jsp"))
                .build();
    }

    @Test
    public void testGetOrder() throws Exception {
        when(orderService.getById(1L)).thenReturn(order);

        mockMvc.perform(get(orderUrl, "1"))
                .andExpect(model().attribute("order", order))
                .andExpect(view().name("admin/orderDetails"));
    }

    @Test
    public void testGetOrderNotFound() throws Exception {
        when(orderService.getById(1L)).thenThrow(OrderNotFoundException.class);

        mockMvc.perform(get(orderUrl, "1"))
                .andExpect(view().name("pageNotFound"));
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        mockMvc.perform(post(orderUrl, "1")
                .param("status", "REJECTED"))
                .andExpect(view().name("redirect:/admin/orders/1"));
    }
}
