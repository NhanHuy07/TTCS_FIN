package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.domain.Product;

import java.util.List;

public interface CartService {

    List<Product> getProductsInCart();

    void addProductToCart(Long productId);

    void removeProductFromCart(Long productId);
}
