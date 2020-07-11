package com.es.core.dao.order;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@ContextConfiguration("classpath:context/testApplicationContext-core.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcOrderDaoIntTest {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

    @Before
    public void init() {
        Phone phone1 = new Phone();
        phone1.setId(1L);
        phone1.setBrand("Apple");
        phone1.setModel("iPhone 6");
        phone1.setPrice(new BigDecimal(500));

        Phone phone2 = new Phone();
        phone2.setId(2L);
        phone2.setBrand("Samsung");
        phone2.setModel("Galaxy S9");
        phone2.setPrice(new BigDecimal(600));

        Stock stock1 = new Stock(phone1, 5, 1);
        Stock stock2 = new Stock(phone2, 4, 2);

        Order order = new Order();
        order.setId(1L);
        order.setSecureId("order1");
        order.setFirstName("name");
        order.setLastName("surname");
        order.setDeliveryAddress("address");
        order.setContactPhoneNo("12345");
        order.setStatus(OrderStatus.NEW);

        OrderItem orderItem1 = new OrderItem(phone1, order, 2L);
        order.setOrderItems(Collections.singletonList(orderItem1));

        phoneDao.save(phone1);
        phoneDao.save(phone2);
        stockDao.save(stock1);
        stockDao.save(stock2);
        orderDao.save(order);
        orderItemDao.insertOrderItems(order);
    }

    @Test
    public void testGetOrderById() {
        Order order = orderDao.getById(1L).get();

        assertEquals((Long) 1L, order.getId());
        assertEquals((Long) 1L, order.getOrderItems().get(0).getPhone().getId());
        assertEquals(OrderStatus.NEW, order.getStatus());
    }

    @Test
    public void testGetEmptyOrderById() {
        Optional<Order> order = orderDao.getById(2L);

        assertFalse(order.isPresent());
    }

    @Test
    public void testGetOrderBySecureId() {
        Order order = orderDao.getBySecureId("order1").get();

        assertEquals((Long) 1L, order.getId());
        assertEquals((Long) 1L, order.getOrderItems().get(0).getPhone().getId());
        assertEquals(OrderStatus.NEW, order.getStatus());
    }

    @Test
    public void testGetEmptyOrderBySecureId() {
        Optional<Order> order = orderDao.getBySecureId("order2");

        assertFalse(order.isPresent());
    }

    @Test
    public void testInsertOrder() {
        Order order = new Order();
        order.setId(3L);
        order.setSecureId("order3");
        order.setFirstName("user3");
        order.setLastName("surname");
        order.setDeliveryAddress("address");
        order.setContactPhoneNo("12345");
        order.setStatus(OrderStatus.REJECTED);

        OrderItem orderItem = new OrderItem(phoneDao.get(2L).get(), order, 3L);
        order.setOrderItems(Collections.singletonList(orderItem));

        orderDao.save(order);
        orderItemDao.insertOrderItems(order);
        Order dbOrder = orderDao.getById(3L).get();

        assertEquals((Long) 2L, dbOrder.getOrderItems().get(0).getPhone().getId());
        assertEquals("order3", dbOrder.getSecureId());
        assertEquals(OrderStatus.REJECTED, dbOrder.getStatus());
    }

    @Test
    public void testInsertOrderWithNullId() {
        Order order = new Order();
        order.setSecureId("order3");
        order.setFirstName("user3");
        order.setLastName("surname");
        order.setDeliveryAddress("address");
        order.setContactPhoneNo("12345");
        order.setStatus(OrderStatus.REJECTED);

        OrderItem orderItem = new OrderItem(phoneDao.get(2L).get(), order, 3L);
        order.setOrderItems(Collections.singletonList(orderItem));
        orderDao.save(order);
        orderItemDao.insertOrderItems(order);

        assertTrue(orderDao.getById(2L).isPresent());
    }

    @Test
    public void testFindAll() {
        List<Order> orders = orderDao.findAll();

        assertEquals(1, orders.size());
        assertEquals((Long) 1L, orders.get(0).getId());
    }
}
