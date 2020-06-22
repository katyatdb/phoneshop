package com.es.core.dao.stock;

import com.es.core.dao.phone.PhoneDao;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@ContextConfiguration("classpath:context/testApplicationContext-core.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcStockDaoIntTest {
    @Resource
    private StockDao stockDao;
    @Resource
    private PhoneDao phoneDao;

    @BeforeTransaction
    public void init() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setBrand("Apple");
        phone.setModel("iPhone 8");
        phone.setPrice(new BigDecimal(700));
        Stock stock = new Stock(phone, 5, 2);

        phoneDao.save(phone);
        stockDao.save(stock);
    }

    @Test
    public void testGetStock() {
        Stock stock = stockDao.get(1L).get();

        assertEquals((Integer) 5, stock.getStock());
    }

    @Test
    public void testGetEmptyStock() {
        Optional<Stock> stock = stockDao.get(2L);

        assertFalse(stock.isPresent());
    }

    @Test
    public void insertStock() {
        Phone phone = new Phone();
        phone.setId(2L);
        phone.setBrand("Samsung");
        phone.setModel("Galaxy S10");
        Stock stock = new Stock(phone, 5, 2);
        phoneDao.save(phone);
        stockDao.save(stock);

        Stock dbStock = stockDao.get(2L).get();

        assertEquals(stock.getPhone().getId(), dbStock.getPhone().getId());
    }

    @Test
    public void updateStock() {
        Stock stock = stockDao.get(1L).get();
        stock.setStock(12);
        stockDao.save(stock);

        assertEquals((Integer) 12, stockDao.get(1L).get().getStock());
    }
}
