package com.micropos.order.service;

import com.micropos.dto.CartDto;
import com.micropos.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(CartDto cart, String time);

    List<Order> getAllOrders();

    Optional<Order> getOrder(Integer orderId);
}
