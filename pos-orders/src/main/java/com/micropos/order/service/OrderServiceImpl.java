package com.micropos.order.service;

import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.OrderDto;
import com.micropos.order.mapper.OrderMapper;
import com.micropos.order.model.Item;
import com.micropos.order.model.Order;
import com.micropos.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;

    private OrderMapper orderMapper;

    private StreamBridge streamBridge;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) { this.orderMapper = orderMapper; }

    @Autowired
    public void setStreamBridge(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public Order createOrder(CartDto cart, String time) {
        Order order = new Order().time(time);
        order = orderRepository.save(order);
        List<Item> items = new ArrayList<>();
        for (CartItemDto cartItem : cart.getItems()) {
            items.add(
                    new Item().id(null)
                            .productId(cartItem.getProduct().getId())
                            .productName(cartItem.getProduct().getName())
                            .unitPrice(cartItem.getProduct().getPrice())
                            .quantity(cartItem.getAmount()));

        }
        order.items(items);
        order = orderRepository.save(order);
        //orderDtoToSend = orderMapper.toOrderDto(order);
        sendOrder(order); // send order to rabbitmq
        return order;
    }

    private void sendOrder(Order order) {
        log.info("send {}", orderMapper.toOrderDto(order).toString());
        streamBridge.send("order-send", orderMapper.toOrderDto(order));
    }


    // use streamBridge
//    private OrderDto orderDtoToSend = new OrderDto().id(-1).time("2022-1-1");
//
//    @Bean
//    public Supplier<OrderDto> supplyOrder() {
//
//        return () -> {
//            log.info("supply {}", orderDtoToSend.toString());
//            return orderDtoToSend;
//        };
//    }

    @Override
    public List<Order> getAllOrders() {
        return Streamable.of(orderRepository.findAll()).toList();
    }

    @Override
    public Optional<Order> getOrder(Integer orderId) {
        return orderRepository.findById(orderId);
    }
}
