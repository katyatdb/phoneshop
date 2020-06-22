package com.es.core.service.cart;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCartServiceTest {
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Mock
    private Stock stock1;
    @Mock
    private Stock stock2;

    @Spy
    private StockDao stockDao;
    @Spy
    private Cart cart;
    @Spy
    private ArrayList<CartItem> cartItems;
    @Spy
    private CartItem cartItem1;
    @Spy
    private CartItem cartItem2;
    @Spy
    private CartRecalculationServiceImpl cartRecalculationService;

    @InjectMocks
    private HttpSessionCartService cartService;

    @Before
    public void init() {
        when(phone1.getId()).thenReturn(1L);
        when(phone2.getId()).thenReturn(2L);
        when(phone1.getPrice()).thenReturn(new BigDecimal(200));
        when(phone2.getPrice()).thenReturn(new BigDecimal(400));
        when(phoneDao.get(1L)).thenReturn(Optional.of(phone1));
        when(phoneDao.get(2L)).thenReturn(Optional.of(phone2));

        when(cartItem1.getPhone()).thenReturn(phone1);
        when(cartItem2.getPhone()).thenReturn(phone2);
        when(cartItem1.getQuantity()).thenReturn(1L);
        cartItems.add(cartItem1);
        when(cart.getCartItems()).thenReturn(cartItems);

        when(stock1.getStock()).thenReturn(2);
        when(stock2.getStock()).thenReturn(5);
        when(stockDao.get(1L)).thenReturn(Optional.of(stock1));
        when(stockDao.get(2L)).thenReturn(Optional.of(stock2));
    }

    @Test
    public void testGetCart() {
        assertEquals(cart, cartService.getCart());
    }

    @Test
    public void testAddPhone() {
        cartService.addPhone(2L, 3L);

        assertEquals(2, cart.getCartItems().size());
    }

    @Test
    public void testAddPhonesWithEqualId() {
        cartItems.clear();
        cartService.addPhone(1L, 1L);
        cartService.addPhone(1L, 1L);

        assertEquals((Long) 2L, cart.getCartItems().get(0).getQuantity());
    }

    @Test
    public void testAddPhoneWithZeroQuantity() {
        cartService.addPhone(2L, 0L);

        assertEquals(1, cart.getCartItems().size());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testAddPhoneWithWrongId() {
        cartService.addPhone(3L, 1L);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddPhoneOutOfStock() {
        cartService.addPhone(1L, 2L);
    }

    @Test
    public void testUpdateCart() {
        Map<Long, Long> idQuantityMap = new HashMap<>();
        idQuantityMap.put(1L, 2L);
        idQuantityMap.put(2L, 4L);
        cartItems.add(cartItem2);
        when(cartItem1.getQuantity()).thenCallRealMethod();

        cartService.update(idQuantityMap);

        assertEquals((Long) 2L, cart.getCartItems().get(0).getQuantity());
        assertEquals((Long) 4L, cart.getCartItems().get(1).getQuantity());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testUpdateCartWithWrongPhoneId() {
        Map<Long, Long> idQuantityMap = new HashMap<>();
        idQuantityMap.put(4L, 2L);

        cartService.update(idQuantityMap);
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateCartOutOfStock() {
        Map<Long, Long> idQuantityMap = new HashMap<>();
        idQuantityMap.put(1L, 20L);
        idQuantityMap.put(2L, 4L);

        cartService.update(idQuantityMap);
    }

    @Test
    public void testUpdateCartItemSetZero() {
        Map<Long, Long> idQuantityMap = new HashMap<>();
        idQuantityMap.put(1L, 0L);

        cartService.update(idQuantityMap);

        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    public void testRemovePhone() {
        cartService.remove(1L);

        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    public void testRecalculateTotalPrice() {
        cartService.addPhone(2L, 3L);

        // 1 * 200 + 400 * 3
        assertEquals(new BigDecimal(1400), cart.getTotalPrice());
    }
}
