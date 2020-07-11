package com.es.core.dao.order;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.util.SqlFields;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {
    private final PhoneDao phoneDao;

    public OrderResultSetExtractor(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Order> orders = new ArrayList<>();
        Map<Long, Order> ids = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong(SqlFields.ORDER_ID);
            Order order = ids.get(id);

            if (order == null) {
                order = new Order();
                order.setId(id);
                order.setSecureId(rs.getString(SqlFields.ORDER_SECURE_ID));
                order.setSubtotal(rs.getBigDecimal(SqlFields.ORDER_SUBTOTAL));
                order.setDeliveryPrice(rs.getBigDecimal(SqlFields.ORDER_DELIVERY_PRICE));
                order.setTotalPrice(rs.getBigDecimal(SqlFields.ORDER_TOTAL_PRICE));
                order.setFirstName(rs.getString(SqlFields.ORDER_FIRST_NAME));
                order.setLastName(rs.getString(SqlFields.ORDER_LAST_NAME));
                order.setDeliveryAddress(rs.getString(SqlFields.ORDER_DELIVERY_ADDRESS));
                order.setContactPhoneNo(rs.getString(SqlFields.ORDER_PHONE_NUMBER));
                order.setStatus(OrderStatus.valueOf(rs.getString(SqlFields.ORDER_STATUS)));
                order.setAdditionalInfo(rs.getString(SqlFields.ORDER_ADDITIONAL_INFO));
                order.setOrderItems(new ArrayList<>());

                ids.put(id, order);
            }

            Long phoneId = rs.getLong(SqlFields.ORDER_ITEMS_PHONE_ID);
            Phone phone = phoneDao.get(phoneId).orElseThrow(ProductNotFoundException::new);
            Long quantity = rs.getLong(SqlFields.ORDER_ITEMS_QUANTITY);

            OrderItem orderItem = new OrderItem(phone, order, quantity);
            order.getOrderItems().add(orderItem);

            orders.add(order);
        }

        return orders;
    }
}