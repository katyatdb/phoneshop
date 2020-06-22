package com.es.core.dao.color;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.color.Color;
import com.es.core.model.phone.Phone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:context/testApplicationContext-core.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcColorDaoIntTest {
    @Resource
    private ColorDao ColorDao;
    @Resource
    private PhoneDao PhoneDao;

    @Test
    public void testInsertColors() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setBrand("Apple");
        phone.setModel("iPhone 8");

        Color color1 = new Color(1L, "blue");
        Color color2 = new Color(2L, "red");
        Set<Color> colors = new HashSet<>(Arrays.asList(color1, color2));

        phone.setColors(colors);
        PhoneDao.save(phone);
        ColorDao.insertColors(colors);
        PhoneDao.updatePhoneColors(phone.getId(), phone.getColors());

        Set<Color> foundColors = PhoneDao.get(1L).get().getColors();

        assertEquals(2, foundColors.size());
    }
}
