package com.es.core.dao.phone;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

@ContextConfiguration("classpath:context/testApplicationContext-core.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcProductDaoIntTest {
    @Resource
    private JdbcPhoneDao jdbcPhoneDao;

    @BeforeTransaction
    public void init() {
        Phone phone1 = new Phone();
        phone1.setId(1L);
        phone1.setBrand("Samsung");
        phone1.setModel("Galaxy S9");

        Phone phone2 = new Phone();
        phone2.setId(2L);
        phone2.setBrand("Samsung");
        phone2.setModel("Galaxy S10");

        Phone phone3 = new Phone();
        phone3.setId(3L);
        phone3.setBrand("Apple");
        phone3.setModel("iPhone 8");

        jdbcPhoneDao.save(phone1);
        jdbcPhoneDao.save(phone2);
        jdbcPhoneDao.save(phone3);
    }

    @Test
    public void testGetPhone() {
        Phone phone = jdbcPhoneDao.get(1L).get();

        assertEquals((Long) 1L, phone.getId());
    }

    @Test
    public void testGetEmptyPhone() {
        Optional<Phone> phone = jdbcPhoneDao.get(4L);

        assertFalse(phone.isPresent());
    }

    @Test
    public void testInsertPhoneWithNullId() {
        Phone phone = new Phone();
        phone.setBrand("LG");
        phone.setModel("G7");
        jdbcPhoneDao.save(phone);

        assertEquals("LG", phone.getBrand());
        assertEquals("G7", phone.getModel());
    }

    @Test
    public void testInsertPhoneWithNewId() {
        Phone phone = new Phone();
        phone.setId(4L);
        phone.setBrand("LG");
        phone.setModel("G7");
        jdbcPhoneDao.save(phone);

        assertEquals((Long) 4L, phone.getId());
    }

    @Test
    public void testUpdatePhone() {
        Phone phone = jdbcPhoneDao.get(1L).get();
        phone.setBrand("Apple");
        jdbcPhoneDao.save(phone);

        assertEquals("Apple", phone.getBrand());
    }

    @Test
    public void testInsertColors() {
        Phone phone = jdbcPhoneDao.get(1L).get();
        Set<Color> colors = new HashSet<>();
        colors.add(new Color(1L, "black"));
        colors.add(new Color(2L, "white"));
        phone.setColors(colors);

        jdbcPhoneDao.save(phone);
        Set<Color> foundColors = jdbcPhoneDao.get(1L).get().getColors();

        assertEquals(2, foundColors.size());
    }

    @Test
    public void testFindAll() {
        List<Phone> phones = jdbcPhoneDao.findAll(0, 2);

        assertEquals(2, phones.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithNegativeOffset() {
        jdbcPhoneDao.findAll(-1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithNegativeLimit() {
        jdbcPhoneDao.findAll(0, -1);
    }
}
