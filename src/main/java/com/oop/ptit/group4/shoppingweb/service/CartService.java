package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.domain.Cart;
import com.oop.ptit.group4.shoppingweb.domain.CartItem;
import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.domain.User;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    Cart getOrCreateCart(User user);
    CartItem addToCart(User user, Product product, int quantity);
    CartItem updateCartItem(Long cartItemId, int quantity);
    void removeCartItem(Long cartItemId);
    void clearCart(User user);
    BigDecimal getCartTotal(Cart cart);
}