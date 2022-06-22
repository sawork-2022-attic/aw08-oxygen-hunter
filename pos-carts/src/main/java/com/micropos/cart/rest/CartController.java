package com.micropos.cart.rest;

import com.micropos.api.CartsApi;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.service.CartService;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class CartController implements CartsApi {

    private CartMapper cartMapper;

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
    }

//    @Autowired
//    public void setCartService(CartService cartService) {
//        this.cartService = cartService;
//    }

    @Override
    public ResponseEntity<CartDto> addItemToCart(Integer cartId, CartItemDto cartItemDto) {
        //return CartsApi.super.addItemToCart(cartId, cartItemDto);
        Optional<Cart> cart = cartService.getCart(cartId);
        if (cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CartDto cartDto = cartMapper.toCartDto(cart.get());
        Item item = cartMapper.toItem(cartItemDto, cartDto);
        Cart cartAfterAdd = cartService.add(cart.get(), item);
        cartDto = cartMapper.toCartDto(cartAfterAdd);
        return ResponseEntity.ok(cartDto);
    }

    @Override
    public ResponseEntity<CartDto> createCart(CartDto cartDto) {

        Cart cart = cartMapper.toCart(cartDto);
        cart = cartService.createCart(cart);
        return ResponseEntity.ok(cartMapper.toCartDto(cart));
    }

    @Override
    public ResponseEntity<List<CartDto>> listCarts() {
        List<CartDto> carts = new ArrayList<>(cartMapper.toCartDtos(cartService.getAllCarts()));
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CartDto> showCartById(Integer cartId) {

        Optional<Cart> cart = cartService.getCart(cartId);
        if (cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CartDto cartDto = cartMapper.toCartDto(cart.get());
        return ResponseEntity.ok(cartDto);
    }

    @Override
    public ResponseEntity<Double> showCartTotal(Integer cartId) {

//        Cart cart = new Cart();
//        Item item1 = new Item();
//        item1.productId("a").productName("abc").unitPrice(2).quantity(2);
//        cart.addItem(item1);
//        Item item2 = new Item();
//        item2.productId("b").productName("bcd").unitPrice(3.1).quantity(1);
//        cart.addItem(item2);
//        Double total = cartService.checkout(cart);

        Double total = cartService.getTotal(cartId);

        if (total == -1d) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(total);
    }

    @Override
    public ResponseEntity<OrderDto> checkoutCart(Integer cartId) {
        Optional<OrderDto> order = cartService.checkout(cartId);
        if (order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order.get());
    }
}
