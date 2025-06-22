package com.oop.ptit.group4.shoppingweb.service.impl;

import com.oop.ptit.group4.shoppingweb.domain.Cart;
import com.oop.ptit.group4.shoppingweb.domain.CartItem;
import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.repository.CartRepository;
import com.oop.ptit.group4.shoppingweb.repository.ProductRepository;
import com.oop.ptit.group4.shoppingweb.service.CartService;
import com.oop.ptit.group4.shoppingweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public CartItem addToCart(User user, Product product, int quantity) {
        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Nếu đã có, cập nhật số lượng
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            entityManager.flush();
            return item;
        } else {
            // Nếu chưa có, tạo mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            cart.addItem(newItem);
            cartRepository.save(cart);
            return newItem;
        }
    }

    @Override
    @Transactional
    public CartItem updateCartItem(Long cartItemId, int quantity) {
        Cart cart = cartRepository.findAll().stream()
                .filter(c -> c.getItems().stream().anyMatch(item -> item.getId().equals(cartItemId)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục trong giỏ hàng"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục trong giỏ hàng"));

        if (quantity <= 0) {
            cart.removeItem(item);
        } else {
            item.setQuantity(quantity);
            item.setPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        cartRepository.save(cart);
        return item;
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        Cart cart = cartRepository.findAll().stream()
                .filter(c -> c.getItems().stream().anyMatch(item -> item.getId().equals(cartItemId)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục trong giỏ hàng"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục trong giỏ hàng"));

        cart.removeItem(item);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    @Override
    public BigDecimal getCartTotal(Cart cart) {
        return cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
