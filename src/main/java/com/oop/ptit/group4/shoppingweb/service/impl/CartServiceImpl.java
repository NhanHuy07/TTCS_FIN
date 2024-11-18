package com.oop.ptit.group4.shoppingweb.service.impl;

import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.repository.ProductRepository;
import com.oop.ptit.group4.shoppingweb.service.CartService;
import com.oop.ptit.group4.shoppingweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public List<Product> getProductsInCart() {
        User user = userService.getAuthenticatedUser();
        return user.getProductList();
    }

    @Override
    @Transactional
    public void addProductToCart(Long productId) {
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.getOne(productId);
        user.getProductList().add(product);
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long productId) {
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.getOne(productId);
        user.getProductList().remove(product);
    }
}
