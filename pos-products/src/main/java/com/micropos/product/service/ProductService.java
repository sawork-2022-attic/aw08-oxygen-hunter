package com.micropos.product.service;

import com.micropos.product.model.Product;

import java.util.List;

public interface ProductService {


    public List<Product> products();

    public Product getProduct(String id);

    public Product randomProduct();
}
