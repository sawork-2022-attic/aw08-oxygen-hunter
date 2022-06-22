package com.micropos.delivery.rest;

import com.micropos.api.DeliveryApi;
import com.micropos.delivery.mapper.DeliveryMapper;
import com.micropos.delivery.service.DeliveryService;
import com.micropos.dto.DeliveryEntryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class DeliveryController implements DeliveryApi {

    private DeliveryMapper deliveryMapper;

    private DeliveryService deliveryService;

    @Autowired
    public void setDeliveryMapper(DeliveryMapper deliveryMapper) { this.deliveryMapper = deliveryMapper; }

    @Autowired
    public void setDeliveryService(DeliveryService deliveryService) { this.deliveryService = deliveryService; }

    @Override
    public ResponseEntity<List<DeliveryEntryDto>> listDelivery() {
        List<DeliveryEntryDto> entries = new ArrayList<>(deliveryMapper.toEntryDtos(deliveryService.getAllEntries()));
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

}
