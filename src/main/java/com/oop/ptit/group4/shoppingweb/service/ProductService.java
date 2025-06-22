package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Product getProductById(Long productId);

    List<Product> getPopularProducts();

    Page<Product> getProductsByFilterParams(SearchRequest searchRequest, Pageable pageable);

    Page<Product> searchProducts(SearchRequest searchRequest, Pageable pageable);

    Optional<Product> findById(Long id);

}

