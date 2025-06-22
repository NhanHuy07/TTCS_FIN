package com.oop.ptit.group4.shoppingweb.dto.request;

import com.oop.ptit.group4.shoppingweb.domain.Order;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CheckOutRequest {

    @NotBlank(message = "Tên không được để trống")
    private String firstName;

    @NotBlank(message = "Họ không được để trống")
    private String lastName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "\\d{10,11}", message = "Số điện thoại phải có 10-11 chữ số")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Thành phố không được để trống")
    private String city;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;

    // Helper method to convert payment method string to enum
    public Order.PaymentMethod getPaymentMethodEnum() {
        try {
            return Order.PaymentMethod.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            return Order.PaymentMethod.COD; // Default to COD if invalid
        }
    }
}
