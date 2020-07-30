package com.es.core.service.stock;

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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {
    @Mock
    private StockDao stockDao;
    @Mock
    private Stock stock1;
    @Mock
    private Stock stock2;
    @Mock
    private Phone phone;

    @InjectMocks
    private StockServiceImpl stockService;

    @Before
    public void init() {
        when(stockDao.get(1L)).thenReturn(Optional.of(stock1));
        when(stock1.getPhone()).thenReturn(phone);
        when(stock1.getStock()).thenReturn(10);
        when(stock1.getReserved()).thenReturn(5);
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

    @Test
    public void testChangeStockToReserved() {
        stockService.changeStockToReserved(1L, 4);

        verify(stock1).setStock(6);
        verify(stock1).setReserved(9);
        verify(stockDao).save(stock1);
    }

    @Test
    public void testChangeReservedToStock() {
        stockService.changeReservedToStock(1L, 4);

        verify(stock1).setStock(14);
        verify(stock1).setReserved(1);
        verify(stockDao).save(stock1);
    }

    @Test
    public void testDeleteReserved() {
        stockService.deleteReserved(1L, 4);

        verify(stock1).setReserved(1);
        verify(stockDao).save(stock1);
    }
}
