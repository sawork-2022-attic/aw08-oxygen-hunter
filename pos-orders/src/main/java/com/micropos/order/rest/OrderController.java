package com.micropos.order.rest;

import com.micropos.api.OrdersApi;
import com.micropos.dto.CartDto;
import com.micropos.dto.OrderDto;
import com.micropos.order.mapper.OrderMapper;
import com.micropos.order.model.Order;
import com.micropos.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class OrderController implements OrdersApi {

    private OrderMapper orderMapper;

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @Override
    public ResponseEntity<OrderDto> createOrder(CartDto cartDto) {
        //System.out.println("order controller: createOrder: " + cartDto.toString());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());

        Order order = orderService.createOrder(cartDto, date);
        OrderDto orderDto = orderMapper.toOrderDto(order);
        return ResponseEntity.ok(orderDto);
    }


    @Override
    public ResponseEntity<List<OrderDto>> listOrders() {
        List<OrderDto> orders = new ArrayList<>(orderMapper.toOrderDtos(orderService.getAllOrders()));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDto> showOrderById(Integer orderId) {

        Optional<Order> order = orderService.getOrder(orderId);
        if (order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        OrderDto orderDto = orderMapper.toOrderDto(order.get());
        return ResponseEntity.ok(orderDto);
    }
}
