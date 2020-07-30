package com.es.core.dao.order;

import com.es.core.dao.AbstractJdbcDao;
import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.order.Order;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcOrderDao extends AbstractJdbcDao<Order> implements OrderDao {
    private static final String FIND_ORDER_BY_ID = "select * from orders join orderItems " +
            "on orders.id = orderItems.orderId where orders.id = :id";
    private static final String FIND_ORDER_BY_SECURE_ID = "select * from orders join orderItems " +
            "on orders.id = orderItems.orderId where orders.secureId = :secureId";
    private static final String INSERT_ORDER = "insert into orders (id, secureId, subtotal, deliveryPrice, " +
            "totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, status, additionalInfo) " +
            "values (:id, :secureId, :subtotal, :deliveryPrice, :totalPrice, :firstName, :lastName, " +
            ":deliveryAddress, :contactPhoneNo, :status, :additionalInfo)";
    private static final String UPDATE_ORDER = "update orders set subtotal = :subtotal, " +
            "deliveryPrice = :deliveryPrice, totalPrice = :totalPrice, firstName = :firstName, lastName = :lastName, " +
            "deliveryAddress = :deliveryAddress, contactPhoneNo = :contactPhoneNo, status = :status, " +
            "additionalInfo = :additionalInfo where id = :id";
    private static final String FIND_ALL_ORDERS = "select * from orders join orderItems " +
            "on orders.id = orderItems.orderId";

    @Resource
    private PhoneDao phoneDao;

    @Override
    public Optional<Order> getById(Long id) {
        List<Order> orders = super.findAll(FIND_ORDER_BY_ID,
                new MapSqlParameterSource("id", id), new OrderResultSetExtractor(phoneDao));

        if (orders.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(orders.get(0));
    }

    @Override
    public Optional<Order> getBySecureId(String secureId) {
        List<Order> orders = super.findAll(FIND_ORDER_BY_SECURE_ID,
                new MapSqlParameterSource("secureId", secureId), new OrderResultSetExtractor(phoneDao));

        if (orders.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(orders.get(0));
    }

    @Override
    public void save(Order order) {
        Optional<Order> orderOptional = getById(order.getId());

        if (orderOptional.isPresent()) {
            update(order);
        } else {
            insert(order);
        }
    }

    @Override
    public List<Order> findAll() {
        return super.findAll(FIND_ALL_ORDERS, new OrderResultSetExtractor(phoneDao));
    }

    private void insert(Order order) {
        Long newId = (Long) super.save(INSERT_ORDER, getSqlOrderParams(order), new GeneratedKeyHolder());

        if (order.getId() == null) {
            order.setId(newId);
        }
    }

    private void update(Order order) {
        super.save(UPDATE_ORDER, getSqlOrderParams(order));
    }

    private SqlParameterSource getSqlOrderParams(Order order) {
        return new MapSqlParameterSource()
                .addValue("id", order.getId())
                .addValue("secureId", order.getSecureId())
                .addValue("subtotal", order.getSubtotal())
                .addValue("deliveryPrice", order.getDeliveryPrice())
                .addValue("totalPrice", order.getTotalPrice())
                .addValue("firstName", order.getFirstName())
                .addValue("lastName", order.getLastName())
                .addValue("deliveryAddress", order.getDeliveryAddress())
                .addValue("contactPhoneNo", order.getContactPhoneNo())
                .addValue("status", order.getStatus().toString())
                .addValue("additionalInfo", order.getAdditionalInfo());
    }
}
