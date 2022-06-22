package com.micropos.delivery.service;

import com.micropos.delivery.model.Entry;
import com.micropos.delivery.repository.DeliveryRepository;
import com.micropos.dto.OrderDto;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    private DeliveryRepository deliveryRepository;

    @Autowired
    public void setDeliveryRepository(DeliveryRepository deliveryRepository) { this.deliveryRepository = deliveryRepository; }

    @Bean
    Consumer<OrderDto> receiveOrder() {
        return orderDto -> {
            log.info("receive: {}",  orderDto);
            createEntry(orderDto);
        };
    }

    @Override
    public Entry createEntry(OrderDto orderDto) {
        Entry entry = new Entry().orderId(orderDto.getId()).status("preparing");
        return deliveryRepository.save(entry);
    }

    @Override
    public List<Entry> getAllEntries() {
        return Streamable.of(deliveryRepository.findAll()).toList();
    }

    @Override
    public Optional<Entry> getEntry(Integer orderId) {
        return deliveryRepository.findById(orderId);
    }
}
