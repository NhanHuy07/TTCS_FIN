package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.domain.Cart;
import com.oop.ptit.group4.shoppingweb.domain.Order;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.dto.request.CheckOutRequest;
import com.oop.ptit.group4.shoppingweb.service.CartService;
import com.oop.ptit.group4.shoppingweb.service.UserService;
import com.oop.ptit.group4.shoppingweb.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/checkout")
public class CheckOutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String checkout() {
        return "redirect:/checkout/";
    }

    @GetMapping("/")
    public String showCheckoutForm(Principal principal, Model model) {
        if (principal == null) {
            System.out.println("Principal is null, redirecting to login");
            return "redirect:/login";
        }

        System.out.println("Principal name: " + principal.getName());

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            System.out.println("User not found for email: " + principal.getName());
            return "redirect:/login";
        }

        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart?error=Giỏ hàng của bạn đang trống";
        }
        BigDecimal total = cartService.getCartTotal(cart);

        // Pre-fill checkout info if available
        CheckOutRequest checkOutRequest = new CheckOutRequest();
        checkOutRequest.setFirstName(user.getFirstName());
        checkOutRequest.setLastName(user.getLastName());
        checkOutRequest.setEmail(user.getEmail());
        checkOutRequest.setPhone(user.getPhoneNumber());
        checkOutRequest.setAddress(user.getAddress());
        checkOutRequest.setPaymentMethod("COD"); // Default payment method

        model.addAttribute("checkOutRequest", checkOutRequest);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "orders/checkout";
    }

    @PostMapping("/")
    public String processCheckout(
            Principal principal,
            @Valid @ModelAttribute("checkOutRequest") CheckOutRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpSession session) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống");
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            BigDecimal total = cartService.getCartTotal(cart);

            model.addAttribute("cart", cart);
            model.addAttribute("total", total);
            return "orders/checkout";
        }

        try {
            // Create order
            Order order = orderService.createOrderFromCart(
                    user,
                    cart,
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getAddress(),
                    request.getCity(),
                    request.getPaymentMethodEnum()
            );

            // Clear cart after successful order
            cartService.clearCart(user);

            // Store order number in session for confirmation page
            session.setAttribute("lastOrderNumber", order.getOrderNumber());

            return "redirect:/checkout/confirmation";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo đơn hàng: " + e.getMessage());
            return "redirect:/checkout/";
        }
    }

    @GetMapping("/confirmation")
    public String showConfirmation(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String orderNumber = (String) session.getAttribute("lastOrderNumber");

        if (orderNumber == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin đơn hàng");
            return "redirect:/cart";
        }

        // Remove from session
        session.removeAttribute("lastOrderNumber");

        Order order = orderService.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy đơn hàng với mã: " + orderNumber));

        model.addAttribute("order", order);

        return "orders/confirmation";
    }
}
