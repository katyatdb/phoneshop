package com.es.core.dao.stock;

import com.es.core.dao.AbstractJdbcDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcStockDao extends AbstractJdbcDao<Stock> implements StockDao {
    private static final String FIND_STOCK_BY_PHONE_ID = "select * from stocks where phoneId = ?";
    private static final String INSERT_STOCK = "insert into stocks (phoneId, stock, reserved) " +
            "values (:phoneId, :stock, :reserved)";
    private static final String UPDATE_STOCK = "update stocks set stock = :stock, reserved = :reserved " +
            "where phoneId = :phoneId";

    @Override
    public Optional<Stock> get(Long phoneId) {
        return super.get(FIND_STOCK_BY_PHONE_ID, new StockRowMapper(), phoneId);
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

    private static class StockRowMapper extends BeanPropertyRowMapper<Stock> {
        @Override
        public Stock mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Phone phone = new Phone();
            phone.setId(rs.getLong("phoneId"));

            Stock stock = new Stock();
            stock.setPhone(phone);
            stock.setStock(rs.getInt("stock"));
            stock.setReserved(rs.getInt("reserved"));

            return stock;
        }
    }
}
