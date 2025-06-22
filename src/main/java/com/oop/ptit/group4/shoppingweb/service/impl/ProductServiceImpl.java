package com.oop.ptit.group4.shoppingweb.service.impl;

import com.oop.ptit.group4.shoppingweb.constants.ErrorMessage;
import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import com.oop.ptit.group4.shoppingweb.repository.ProductRepository;
import com.oop.ptit.group4.shoppingweb.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.LAPTOP_NOT_FOUND));
    }

    @Override
    public List<Product> getPopularProducts() {
        List<Long> productIds = Arrays.asList(26L, 43L, 46L, 16L, 34L, 7L, 8L, 5L, 27L, 39L, 9L, 6L);
        return productRepository.findByIdIn(productIds);
    }

    @Override
    public Page<Product> getProductsByFilterParams(SearchRequest request, Pageable pageable) {
        BigDecimal startingPrice = request.getPrice();
        BigDecimal endingPrice = startingPrice.add(startingPrice.compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal("40000000") : new BigDecimal("5000000"));
        return productRepository.getProductsByFilterParams(
                request.getBrands(),
                request.getColors(),
                startingPrice,
                endingPrice,
                pageable);
    }

    @Override
    public Page<Product> searchProducts(SearchRequest request, Pageable pageable) {
        return productRepository.searchProducts(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}