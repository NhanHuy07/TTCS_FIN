package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface ProductService {

    Product getProductById(Long productId);

    List<Product> getPopularProducts();

    Page<Product> getProductsByFilterParams(SearchRequest searchRequest, Pageable pageable);

    Page<Product> searchProducts(SearchRequest searchRequest, Pageable pageable);
}

