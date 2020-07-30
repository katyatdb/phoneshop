package com.es.core.service.stock;

import com.es.core.dao.stock.StockDao;
import com.es.core.exception.StockNotFoundException;
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

    @Override
    public void changeStockToReserved(Long phoneId, int quantity) {
        Stock stock = stockDao.get(phoneId).orElseThrow(StockNotFoundException::new);

        if (quantity > stock.getStock()) {
            throw new IllegalStateException();
        }

        stock.setStock(stock.getStock() - quantity);
        stock.setReserved(stock.getReserved() + quantity);
        stockDao.save(stock);
    }

    @Override
    public void changeReservedToStock(Long phoneId, int quantity) {
        Stock stock = stockDao.get(phoneId).orElseThrow(StockNotFoundException::new);

        if (quantity > stock.getReserved()) {
            throw new IllegalStateException();
        }

        stock.setStock(stock.getStock() + quantity);
        stock.setReserved(stock.getReserved() - quantity);
        stockDao.save(stock);
    }

    @Override
    public void deleteReserved(Long phoneId, int quantity) {
        Stock stock = stockDao.get(phoneId).orElseThrow(StockNotFoundException::new);

        if (quantity > stock.getReserved()) {
            throw new IllegalStateException();
        }

        stock.setReserved(stock.getReserved() - quantity);
        stockDao.save(stock);
    }
}
