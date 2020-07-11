package com.es.core.dao.order;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:context/testApplicationContext-core.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcOrderItemDaoIntTest {
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private PhoneDao phoneDao;

    @Test
    public void testInsertOrderItems() {
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

        Order order = new Order();
        order.setId(1L);
        order.setSecureId("order3");
        order.setFirstName("user3");
        order.setLastName("surname");
        order.setDeliveryAddress("address");
        order.setContactPhoneNo("12345");
        order.setStatus(OrderStatus.REJECTED);

        OrderItem orderItem1 = new OrderItem(phone1, order, 3L);
        OrderItem orderItem2 = new OrderItem(phone2, order, 5L);
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));

        phoneDao.save(phone1);
        phoneDao.save(phone2);
        orderDao.save(order);
        orderItemDao.insertOrderItems(order);
        Order dbOrder = orderDao.getById(1L).get();

        assertEquals(2, dbOrder.getOrderItems().size());
        assertEquals((Long) 1L, dbOrder.getOrderItems().get(0).getPhone().getId());
        assertEquals((Long) 2L, dbOrder.getOrderItems().get(1).getPhone().getId());
    }
}
