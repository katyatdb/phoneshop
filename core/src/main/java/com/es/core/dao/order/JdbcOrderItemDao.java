package com.es.core.dao.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class JdbcOrderItemDao implements OrderItemDao {
    private static final String INSERT_ORDER_ITEM = "insert into orderItems (phoneId, orderId, quantity) " +
            "values (:phoneId, :orderId, :quantity)";

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void insertOrderItems(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        SqlParameterSource[] sqlParameterSources = new SqlParameterSource[orderItems.size()];

        for (int i = 0; i < orderItems.size(); i++) {
            sqlParameterSources[i] = new MapSqlParameterSource()
                    .addValue("phoneId", orderItems.get(i).getPhone().getId())
                    .addValue("orderId", orderItems.get(i).getOrder().getId())
                    .addValue("quantity", orderItems.get(i).getQuantity());
        }

        namedParameterJdbcTemplate.batchUpdate(INSERT_ORDER_ITEM, sqlParameterSources);
    }
}
