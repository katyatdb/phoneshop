package com.es.core.service.stock;

import com.es.core.dao.stock.StockDao;
import com.es.core.model.stock.Stock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {
    @Resource
    private StockDao stockDao;

    @Override
    public Optional<Stock> getStock(Long phoneId) {
        return stockDao.get(phoneId);
    }

    @Override
    public void save(Stock stock) {
        stockDao.save(stock);
    }
}
