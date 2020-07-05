package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.cart.CartService;
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
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class CartPageControllerTest {
    private static final String cartUrl = "/cart";
    private static final String cartItemsFormJson = "{\"cartItems[0].id\": \"1\", \"cartItems[0].quantity\": \"2\"}";
    private MockMvc mockMvc;

    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private CartItem cartItem1;
    @Mock
    private CartItem cartItem2;
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Spy
    private ArrayList<CartItem> cartItems;

    @InjectMocks
    private CartPageController cartPageController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartPageController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/cart.jsp"))
                .build();

        when(cartService.getCart()).thenReturn(cart);
        when(cart.getCartItems()).thenReturn(cartItems);
        when(cartItem1.getPhone()).thenReturn(phone1);
        when(cartItem2.getPhone()).thenReturn(phone2);
        when(cartItem1.getQuantity()).thenReturn(1L);
        when(cartItem2.getQuantity()).thenReturn(2L);
        when(phone1.getId()).thenReturn(1L);
        when(phone2.getId()).thenReturn(2L);
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);
    }

    @Test
    public void testGetCart() throws Exception {
        mockMvc.perform(get(cartUrl))
                .andExpect(model().attributeExists("cartItemListForm"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(view().name("cart"));
    }

    @Test
    public void testUpdateCart() throws Exception {
        mockMvc.perform(put(cartUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartItemsFormJson))
                .andExpect(model().attributeExists("cartItemListForm"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(view().name("redirect:/cart"));
    }

    @Test
    public void testUpdateCartOutOfStock() throws Exception {
        doThrow(new OutOfStockException(Collections.singletonList(1L)))
                .when(cartService).update(anyMap());

        mockMvc.perform(put(cartUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartItemsFormJson))
                .andExpect(model().attributeExists("cartItemListForm"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(model().attributeHasFieldErrors("cartItemListForm", "cartItems[0].quantity"))
                .andExpect(model().attributeHasFieldErrorCode("cartItemListForm", "cartItems[0].quantity", "validation.outOfStock"))
                .andExpect(view().name("cart"));
    }

    @Test
    public void testDeletePhone() throws Exception {
        String request = "\"id\": \"1\"";

        mockMvc.perform(delete(cartUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(model().attributeExists("cartItemListForm"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(view().name("redirect:/cart"));
    }
}
