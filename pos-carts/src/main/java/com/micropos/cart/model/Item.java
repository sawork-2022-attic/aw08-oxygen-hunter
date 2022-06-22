package com.micropos.cart.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "items")
@Accessors(fluent = true, chain = true)
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Column(name="cart_id")
    @Getter
    @Setter
    private Integer cartId;

    @Column(name = "product_id")
    @Getter
    @Setter
    private String productId;

    @Column(name = "product_name")
    @Getter
    @Setter
    private String productName;

    @Column(name = "unit_price")
    @Getter
    @Setter
    private double unitPrice;

    @Column(name = "quantity")
    @Getter
    @Setter
    private int quantity;
}
