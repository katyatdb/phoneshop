package com.es.core.dao.stock;

import com.es.core.dao.AbstractJdbcDao;
import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.stock.Stock;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class JdbcStockDao extends AbstractJdbcDao<Stock> implements StockDao {
    private static final String FIND_STOCK_BY_PHONE_ID = "select * from stocks where phoneId = :phoneId";
    private static final String INSERT_STOCK = "insert into stocks (phoneId, stock, reserved) " +
            "values (:phoneId, :stock, :reserved)";
    private static final String UPDATE_STOCK = "update stocks set stock = :stock, reserved = :reserved " +
            "where phoneId = :phoneId";

    @Resource
    private PhoneDao phoneDao;

    @Override
    public Optional<Stock> get(Long phoneId) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("phoneId", phoneId);
        return super.get(FIND_STOCK_BY_PHONE_ID, sqlParameterSource, new StockRowMapper(phoneDao));
    }

    @Override
    public void save(Stock stock) {
        Optional<Stock> stockOptional = get(stock.getPhone().getId());

        if (stockOptional.isPresent()) {
            update(stock);
        } else {
            insert(stock);
        }
    }

    private void insert(Stock stock) {
        super.save(INSERT_STOCK, getSqlStockParams(stock));
    }

    private void update(Stock stock) {
        super.save(UPDATE_STOCK, getSqlStockParams(stock));
    }

    private SqlParameterSource getSqlStockParams(Stock stock) {
        return new MapSqlParameterSource()
                .addValue("phoneId", stock.getPhone().getId())
                .addValue("stock", stock.getStock())
                .addValue("reserved", stock.getReserved());
    }
}
