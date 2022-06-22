package com.micropos.product.rest;

import com.micropos.api.ProductsApi;
import com.micropos.dto.ProductDto;
import com.micropos.product.mapper.ProductMapper;
import com.micropos.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class ProductController implements ProductsApi {

    private final ProductMapper productMapper;

    private final ProductService productService;


    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @Override
    public ResponseEntity<List<ProductDto>> listProducts(){
        List<ProductDto> products = new ArrayList<>(productMapper.toProductsDto(this.productService.products()));
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
