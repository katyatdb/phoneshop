package com.es.core.service.phone;

import com.es.core.dao.color.ColorDao;
import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhoneServiceImplTest {
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private Phone phone;
    @Mock
    private ColorDao colorDao;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    @Before
    public void init() {
        when(phoneDao.get(1L)).thenReturn(Optional.of(phone));
    }

    @Test
    public void testGetPhone() {
        Optional<Phone> phoneOptional = phoneService.getPhone(1L);

        assertEquals(phone, phoneOptional.get());
    }

    @Test
    public void testSavePhone() {
        phoneService.save(phone);

        verify(phoneDao).save(phone);
        verify(colorDao).insertColors(phone.getColors());
        verify(phoneDao).updatePhoneColors(phone.getId(), phone.getColors());
    }

    @Test
    public void testGetPhoneList() {
        phoneService.getPhoneList(null, 2, 10, "brand", "asc");

        verify(phoneDao).findAll(null, 10, 10, "brand", "asc");
    }

    @Test
    public void testGetPhonesNumber() {
        phoneService.getPhonesNumber(null);

        verify(phoneDao).countPhones(null);
    }
}
