package com.oop.ptit.group4.shoppingweb.service.impl;

import com.oop.ptit.group4.shoppingweb.constants.ErrorMessage;
import com.oop.ptit.group4.shoppingweb.constants.SuccessMessage;
import com.oop.ptit.group4.shoppingweb.domain.*;
import com.oop.ptit.group4.shoppingweb.dto.request.ProductRequest;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import com.oop.ptit.group4.shoppingweb.dto.response.MessageResponse;
import com.oop.ptit.group4.shoppingweb.dto.response.UserInfoResponse;
import com.oop.ptit.group4.shoppingweb.repository.OrderRepository;
import com.oop.ptit.group4.shoppingweb.repository.ProductRepository;
import com.oop.ptit.group4.shoppingweb.repository.UserRepository;
import com.oop.ptit.group4.shoppingweb.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Product> searchProducts(SearchRequest request, Pageable pageable) {
        return productRepository.searchProducts(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(SearchRequest request, Pageable pageable) {
        return userRepository.searchUsers(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public MessageResponse lockUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Kiểm tra nếu người dùng có vai trò ADMIN
            if (user.getRoles().contains(Role.ADMIN)) {
                return new MessageResponse("alert-danger", "Không thể khóa tài khoản của Admin!");
            }
            // Nếu không phải ADMIN, tiến hành khóa tài khoản
            user.setActive(false);
            userRepository.save(user);
            return new MessageResponse("alert-success", "Đã khóa thành công");
        }
        // Trường hợp không tìm thấy người dùng
        return new MessageResponse("alert-danger", "Không tìm thấy người dùng!");
    }

    @Override
    public MessageResponse unlockUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        userOpt.ifPresent(user -> {
            user.setActive(true);
            userRepository.save(user);
        });
        return new MessageResponse("alert-success", "Mở khóa thành công");
    }

    @Override
    public MessageResponse confirmOrder(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id); // Sử dụng findById thay vì getById
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            // Kiểm tra trạng thái đơn hàng
            if (order.getStatus() == Order.OrderStatus.CANCELED) {
                return new MessageResponse("alert-danger", "Không thể xác nhận đơn hàng đã bị hủy!");
            }
            if (order.getStatus() == Order.OrderStatus.DELIVERED) {
                return new MessageResponse("alert-success", "Đơn hàng đã được xác nhận giao!");
            }

            // Cập nhật số lượng tồn kho của sản phẩm
            for (OrderItem item : order.getOrderItems()) { // Giả định Order có danh sách OrderItem
                Product product = item.getProduct();
                int quantity = item.getQuantity();
                if (product.getStock() < quantity) {
                    return new MessageResponse("alert-danger", "Sản phẩm " + product.getProductTitle() + " không đủ hàng trong kho!");
                }
                product.setStock(product.getStock() - quantity); // Giảm số lượng tồn kho
                productRepository.save(product); // Lưu thay đổi vào cơ sở dữ liệu
            }

            // Cập nhật trạng thái đơn hàng thành DELIVERED
            order.setStatus(Order.OrderStatus.DELIVERED);
            orderRepository.save(order);

            return new MessageResponse("alert-success", "Xác nhận giao hàng thành công!");
        }
        return new MessageResponse("alert-danger", "Không tìm thấy đơn hàng!");
    }

    @Override
    public MessageResponse deleteOrder(Long id) {
        Optional<Order> orderOpt = orderRepository.getById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            // Kiểm tra trạng thái đơn hàng
            if (order.getStatus() != Order.OrderStatus.CANCELED) {
                return new MessageResponse("alert-danger", "Chỉ có thể xóa đơn hàng có trạng thái bị hủy!");
            }
            // Xóa đơn hàng
            orderRepository.delete(order);
            return new MessageResponse("alert-success", "Xóa đơn hàng thành công!");
        }
        return new MessageResponse("alert-danger", "Không tìm thấy đơn hàng!");
    }

    @Override
    public MessageResponse addUser(User user) {
        // Kiểm tra email đã tồn tại
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return new MessageResponse("alert-danger", "Email đã được sử dụng!");
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Đặt giá trị mặc định nếu cần
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>(Arrays.asList(Role.USER))); // Mặc định là USER
        }

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
        return new MessageResponse("alert-success", "Thêm người dùng thành công!");
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.getById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND));
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);

    }

    @Override
    public Page<Order> searchOrders(SearchRequest request, Pageable pageable) {
        return orderRepository.searchOrders(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.LAPTOP_NOT_FOUND));
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse editProduct(ProductRequest productRequest, MultipartFile file) {
        return saveProduct(productRequest, file, SuccessMessage.LAPTOP_EDITED);
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse addProduct(ProductRequest productRequest, MultipartFile file) {
        return saveProduct(productRequest, file, SuccessMessage.LAPTOP_ADDED);
    }

    @Override
    public UserInfoResponse getUserById(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        Page<Order> orders = orderRepository.findOrderByUserId(userId, pageable);
        return new UserInfoResponse(user, orders);
    }

    private MessageResponse saveProduct(ProductRequest productRequest, MultipartFile file, String message) throws IOException {
        Product product = modelMapper.map(productRequest, Product.class);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            product.setFilename(resultFilename);
        }
        productRepository.save(product);
        return new MessageResponse("alert-success", message);
    }
}
