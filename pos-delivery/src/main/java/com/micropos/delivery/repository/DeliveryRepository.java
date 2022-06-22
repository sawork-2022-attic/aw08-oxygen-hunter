package com.micropos.delivery.repository;

import com.micropos.delivery.model.Entry;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryRepository extends CrudRepository<Entry, Integer> {

}
