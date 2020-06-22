package com.es.core.service.stock;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private Phone phone;
    @Mock
    private StockDao stockDao;
    @Mock
    private Stock stock1;
    @Mock
    private Stock stock2;

    @InjectMocks
    private StockServiceImpl stockService;

    @Before
    public void init() {
        when(stockDao.get(1L)).thenReturn(Optional.of(stock1));
        when(phoneDao.get(1L)).thenReturn(Optional.of(phone));
        when(stock1.getPhone()).thenReturn(phone);
    }

    @Test
    public void testGetStock() {
        Optional<Stock> stock = stockService.getStock(1L);

        assertEquals(phone, stock.get().getPhone());
    }

    @Test
    public void testSaveStock() {
        stockService.save(stock2);

        verify(stockDao).save(stock2);
    }
}
