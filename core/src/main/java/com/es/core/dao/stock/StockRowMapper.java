package com.es.core.dao.stock;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import com.es.core.util.SqlFields;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockRowMapper extends BeanPropertyRowMapper<Stock> {
    private final PhoneDao phoneDao;

    public StockRowMapper(PhoneDao phoneDao) {
        super(Stock.class);
        this.phoneDao = phoneDao;
    }

    @Override
    public Stock mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Long phoneId = rs.getLong(SqlFields.STOCKS_PHONE_ID);
        Phone phone = phoneDao.get(phoneId).orElseThrow(ProductNotFoundException::new);

        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(rs.getInt(SqlFields.STOCKS_STOCK));
        stock.setReserved(rs.getInt(SqlFields.STOCKS_RESERVED));

        return stock;
    }
}
