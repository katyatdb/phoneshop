package com.es.phoneshop.web.controller;

import com.es.core.exception.OutOfStockException;
import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.exception.RestResponseEntityExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AjaxCartControllerTest {
    private static final String ajaxCartUrl = "/ajaxCart";
    private MockMvc mockMvc;

    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private CartItem cartItem;
    @Spy
    private ArrayList<CartItem> cartItems;

    @InjectMocks
    private AjaxCartController ajaxCartController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(ajaxCartController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        when(cartService.getCart()).thenReturn(cart);
        when(cart.getCartItems()).thenReturn(cartItems);
    }

    @Test
    public void testGetCart() throws Exception {
        when(cartItems.size()).thenReturn(2);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(200));

        mockMvc.perform(get(ajaxCartUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.cartItemsQuantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    public void testAddPhone() throws Exception {
        doAnswer(invocation -> cartItems.add(cartItem))
                .when(cartService).addPhone(1L, 2L);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(200));
        String cartItemJson = "{\"id\": \"1\", \"quantity\": \"2\"}";

        mockMvc.perform(post(ajaxCartUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartItemJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.cartItemsQuantity").value(1))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    public void testAddPhoneWithWrongId() throws Exception {
        String cartItemJson = "{\"id\": \"two\", \"quantity\": \"1\"}";

        mockMvc.perform(post(ajaxCartUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartItemJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid format"));
    }

    @Test
    public void testAddPhoneOutOfStock() throws Exception {
        doThrow(OutOfStockException.class)
                .when(cartService).addPhone(1L, 2L);
        String cartItemJson = "{\"id\": \"1\", \"quantity\": \"2\"}";

        mockMvc.perform(post(ajaxCartUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartItemJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(result.getResolvedException().getClass(),
                        OutOfStockException.class))
                .andExpect(content().string("Out of stock"));
    }

    @Test
    public void testAddPhoneNotFound() throws Exception {
        doThrow(ProductNotFoundException.class)
                .when(cartService).addPhone(1L, 2L);
        String cartItemJson = "{\"id\": \"1\", \"quantity\": \"2\"}";

        mockMvc.perform(post(ajaxCartUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartItemJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(result.getResolvedException().getClass(),
                        ProductNotFoundException.class))
                .andExpect(content().string("Product not found"));
    }
}
