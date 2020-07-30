package com.es.core.service.stock;

import com.es.core.model.stock.Stock;

import java.util.Optional;

public interface StockService {
    Optional<Stock> getStock(Long phoneId);

    void save(Stock stock);

    void changeStockToReserved(Long phoneId, int quantity);

    void changeReservedToStock(Long phoneId, int quantity);

    void deleteReserved(Long phoneId, int quantity);
}
