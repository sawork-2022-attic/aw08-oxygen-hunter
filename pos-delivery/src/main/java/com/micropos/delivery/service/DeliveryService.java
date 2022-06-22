package com.micropos.delivery.service;

import com.micropos.delivery.model.Entry;
import com.micropos.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {

    Entry createEntry(OrderDto orderDto);

    List<Entry> getAllEntries();

    Optional<Entry> getEntry(Integer orderId);
}
