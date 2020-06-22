package com.es.core.dao.phone;

import com.es.core.dao.stock.StockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@ContextConfiguration("classpath:context/testApplicationContext-core.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcProductDaoIntTest {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

    @BeforeTransaction
    public void init() {
        Phone phone1 = new Phone();
        phone1.setId(1L);
        phone1.setBrand("Samsung");
        phone1.setModel("Galaxy S9");
        phone1.setPrice(new BigDecimal(400));

        Phone phone2 = new Phone();
        phone2.setId(2L);
        phone2.setBrand("Samsung");
        phone2.setModel("Galaxy S10");
        phone2.setPrice(new BigDecimal(500));

        Phone phone3 = new Phone();
        phone3.setId(3L);
        phone3.setBrand("Apple");
        phone3.setModel("iPhone 8");
        phone3.setPrice(new BigDecimal(600));

        Stock stock1 = new Stock(phone1, 5, 3);
        Stock stock2 = new Stock(phone2, 4, 2);
        Stock stock3 = new Stock(phone3, 3, 1);

        phoneDao.save(phone1);
        phoneDao.save(phone2);
        phoneDao.save(phone3);

        stockDao.save(stock1);
        stockDao.save(stock2);
        stockDao.save(stock3);
    }

    @Test
    public void testGetPhone() {
        Phone phone = phoneDao.get(1L).get();

        assertEquals((Long) 1L, phone.getId());
    }

    @Test
    public void testGetEmptyPhone() {
        Optional<Phone> phone = phoneDao.get(4L);

        assertFalse(phone.isPresent());
    }

    @Test
    public void testInsertPhoneWithNullId() {
        Phone phone = new Phone();
        phone.setBrand("LG");
        phone.setModel("G7");
        phoneDao.save(phone);

        assertEquals("LG", phone.getBrand());
        assertEquals("G7", phone.getModel());
    }

    @Test
    public void testInsertPhoneWithNewId() {
        Phone phone = new Phone();
        phone.setId(4L);
        phone.setBrand("LG");
        phone.setModel("G7");
        phoneDao.save(phone);

        assertEquals((Long) 4L, phone.getId());
    }

    @Test
    public void testUpdatePhone() {
        Phone phone = phoneDao.get(1L).get();
        phone.setBrand("Apple");
        phoneDao.save(phone);

        assertEquals("Apple", phone.getBrand());
    }

    @Test
    public void testFindAll() {
        List<Phone> phones = phoneDao.findAll(0, 2);

        assertEquals(2, phones.size());
    }

    @Test
    public void testFindNullPricePhones() {
        Phone phone = new Phone();
        phone.setId(4L);
        phone.setBrand("LG");
        phone.setModel("G7");
        Stock stock = new Stock(phone, 5, 2);
        phoneDao.save(phone);
        stockDao.save(stock);

        List<Phone> phones = phoneDao.findAll(0, 5);

        assertEquals(3, phones.size());
    }

    @Test
    public void testFindZeroStockPhones() {
        Phone phone = new Phone();
        phone.setId(4L);
        phone.setBrand("LG");
        phone.setModel("G7");
        phone.setPrice(new BigDecimal(600));
        Stock stock = new Stock(phone, 0, 2);
        phoneDao.save(phone);
        stockDao.save(stock);

        List<Phone> phones = phoneDao.findAll(0, 5);

        assertEquals(3, phones.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithNegativeOffset() {
        phoneDao.findAll(-1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithNegativeLimit() {
        phoneDao.findAll(0, -1);
    }

    @Test
    public void testFindPhonesWithQuery() {
        List<Phone> phones = phoneDao.findAll("apple", 0, 10, "brand", "asc");

        assertEquals("iPhone 8", phones.get(0).getModel());
    }

    @Test
    public void testFindPhonesWithNullQuery() {
        List<Phone> phones = phoneDao.findAll(null, 0, 10, "brand", "asc");

        assertEquals(3, phones.size());
    }

    @Test
    public void testFindPhonesWithEmptyQuery() {
        List<Phone> phones = phoneDao.findAll("", 0, 10, "brand", "asc");

        assertEquals(3, phones.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPhonesWillNullSort() {
        phoneDao.findAll("apple", 0, 10, null, "asc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPhonesWillNullOrder() {
        phoneDao.findAll("apple", 0, 10, "brand", null);
    }

    @Test
    public void testSortByModelAsc() {
        List<Phone> phones = phoneDao.findAll("", 0, 10, "model", "asc");
        List<String> models = phones
                .stream()
                .map(Phone::getModel)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList("Galaxy S10", "Galaxy S9", "iPhone 8"), models);
    }

    @Test
    public void testSortByPriceDesc() {
        List<Phone> phones = phoneDao.findAll("", 0, 10, "price", "desc");
        List<Long> ids = phones
                .stream()
                .map(Phone::getId)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(3L, 2L, 1L), ids);
    }
}
