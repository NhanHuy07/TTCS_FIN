package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.domain.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemService {

    List<OrderItem> findAll();
    Optional<OrderItem> findById(Long id);
    List<OrderItem> findByOrderId(Long orderId);
    OrderItem save(OrderItem orderItem);
    void deleteById(Long id);
}
