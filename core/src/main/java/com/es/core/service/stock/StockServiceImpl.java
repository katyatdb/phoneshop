package com.es.core.service.stock;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {
    @Resource
    private StockDao stockDao;
    @Resource
    private PhoneDao phoneDao;

    @Override
    public Optional<Stock> getStock(Long phoneId) {
        Optional<Stock> stockOptional = stockDao.get(phoneId);

        if (stockOptional.isPresent()) {
            Phone phone = phoneDao.get(phoneId).orElseThrow(NoSuchElementException::new);
            stockOptional.get().setPhone(phone);
        }

        return stockOptional;
    }

    @Override
    public void save(Stock stock) {
        stockDao.save(stock);
    }
}
