package com.micropos.counter.web;

import com.micropos.api.CounterApi;
import com.micropos.counter.service.CounterService;
import com.micropos.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController implements CounterApi {

    private CounterService counterService;

    @Autowired
    public void setCounterService(CounterService counterService) {
        this.counterService = counterService;
    }

    @Override
    public ResponseEntity<Double> checkout(CartDto cartDto) {
        return ResponseEntity.ok(this.counterService.getTotal(cartDto));
    }

}
