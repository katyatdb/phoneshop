package com.es.core.dao.order;

import com.es.core.model.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> getById(Long id);

    Optional<Order> getBySecureId(String secureId);

    void save(Order order);

    List<Order> findAll();
}
