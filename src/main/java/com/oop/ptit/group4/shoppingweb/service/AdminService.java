package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.domain.Order;
import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.dto.request.ProductRequest;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import com.oop.ptit.group4.shoppingweb.dto.response.MessageResponse;
import com.oop.ptit.group4.shoppingweb.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    Page<Product> getProducts(Pageable pageable);

    Page<Product> searchProducts(SearchRequest request, Pageable pageable);

    Page<User> getUsers(Pageable pageable);

    Page<User> searchUsers(SearchRequest request, Pageable pageable);

    Order getOrder(Long orderId);

    Page<Order> getOrders(Pageable pageable);

    Page<Order> searchOrders(SearchRequest request, Pageable pageable);

    Product getProductById(Long productId);

    MessageResponse editProduct(ProductRequest productRequest, MultipartFile file);

    MessageResponse addProduct(ProductRequest productRequest, MultipartFile file);

    UserInfoResponse getUserById(Long userId, Pageable pageable);

    MessageResponse lockUser(Long id);

    MessageResponse unlockUser(Long id);

    MessageResponse confirmOrder(Long id);

    MessageResponse deleteOrder(Long id);

    MessageResponse addUser(User user);
}
