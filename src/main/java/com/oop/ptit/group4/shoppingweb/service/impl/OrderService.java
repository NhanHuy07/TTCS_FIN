package com.oop.ptit.group4.shoppingweb.service.impl;

import com.oop.ptit.group4.shoppingweb.domain.*;
import com.oop.ptit.group4.shoppingweb.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService{

    @Autowired
    private OrderRepository orderRepository;
    private final MailService mailService;

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllOrderByCreatedAtDesc();
    }

    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Transactional
    public Order createOrderFromCart(User user, Cart cart, String firstName,String lastName, String email,
                                     String phone, String address, String city,
                                     Order.PaymentMethod paymentMethod) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Giỏ hàng trống");
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setEmail(email);
        order.setPhoneNumber(phone);
        order.setAddress(address);
        order.setCity(city);
        order.setPaymentMethod(paymentMethod);

        // Tính tổng tiền
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        // Lấy các order item từ cart item
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setProductName(cartItem.getProduct().getProductTitle());
            orderItem.setProductImage(cartItem.getProduct().getFilename());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.calculateTotal();

            order.addOrderItem(orderItem);
        }

        // Tạo payment
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(Payment.PaymentStatus.PENDING);

        order.setPaymentInfo(payment);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("order", order);
        mailService.sendMessageHtml(order.getEmail(), "Order #" + order.getOrderNumber(), "order-template", attributes);

        // Lưu order
        return orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Order order, Order.OrderStatus status) {
        if (order.getStatus() == status) {
            return;
        }

        order.setStatus(status);

        switch (status) {
            case CONFIRMED:
                order.setConfirmedAt(LocalDateTime.now());
                break;
            case SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                if (order.getPaymentMethod() == Order.PaymentMethod.COD) {
                    order.setPaymentStatus(Order.PaymentStatus.PAID);
                    order.getPayment().setStatus(Payment.PaymentStatus.COMPLETED);
                    order.getPayment().setPaymentDate(LocalDateTime.now());
                }
                break;
            case CANCELED:
                order.setCanceledAt(LocalDateTime.now());
                break;
            default:
                break;
        }

        orderRepository.save(order);
    }

    @Transactional
    public void updatePaymentStatus(Order order, String transactionId, Payment.PaymentStatus status) {
        Payment payment = order.getPayment();

        payment.setTransactionId(transactionId);
        payment.setStatus(status);

        if (status == Payment.PaymentStatus.COMPLETED) {
            payment.setPaymentDate(LocalDateTime.now());
            order.setPaymentStatus(Order.PaymentStatus.PAID);
        } else {
            order.setPaymentStatus(Order.PaymentStatus.UNPAID);
        }

        orderRepository.save(order);
    }

    private String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePrefix = LocalDateTime.now().format(formatter);

        // Tạo 6 số ngẫu nhiên
        Random random = new Random();
        String randomDigits = String.format("%06d", random.nextInt(1000000));

        return "ORD" + datePrefix + randomDigits;
    }
}