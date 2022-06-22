package com.micropos.cart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.repository.CartRepository;
import com.micropos.dto.CartDto;
import com.micropos.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    private final String COUNTER_URL = "http://POS-COUNTER/counter/";

    private final String ORDER_URL = "http://POS-ORDERS/api/orders/";

    private CartMapper cartMapper;

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    private HttpEntity<String> buildCartRequest(Cart cart) {
        CartDto cartDto = cartMapper.toCartDto(cart);
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = null;
        try {
            request = new HttpEntity<>(mapper.writeValueAsString(cartDto), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return request;
    }

    @Override
    public Double getTotal(Cart cart) {
        HttpEntity<String> request = buildCartRequest(cart);
        return restTemplate.postForObject(COUNTER_URL + "/checkout", request, Double.class);
    }

    @Override
    public OrderDto checkout(Cart cart) {
        HttpEntity<String> request = buildCartRequest(cart);
        OrderDto orderDto = restTemplate.postForObject(ORDER_URL, request, OrderDto.class);
        cartRepository.delete(cart); //remove cart from repository
        return orderDto;
    }

    @Override
    public Double getTotal(Integer cartId) {
        Optional<Cart> cart = this.cartRepository.findById(cartId);

        if (cart.isEmpty()) return (double) -1;

        return this.getTotal(cart.get());
    }

    @Override
    public Optional<OrderDto> checkout(Integer cartId) {
        Optional<Cart> cart = this.cartRepository.findById(cartId);

        if (cart.isEmpty()) return Optional.empty();

        return Optional.of(this.checkout(cart.get()));
    }



    @Override
    public Cart add(Cart cart, Item item) {
        if (cart.addItem(item))
            return cartRepository.save(cart);
        return null;
    }

    @Override
    public List<Cart> getAllCarts() {
        return Streamable.of(cartRepository.findAll()).toList();
    }

    @Override
    public Optional<Cart> getCart(Integer cartId) {
        return cartRepository.findById(cartId);
    }
}
