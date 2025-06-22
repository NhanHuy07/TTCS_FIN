package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.constants.PathConstants;
import com.oop.ptit.group4.shoppingweb.domain.Order;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.service.UserService;
import com.oop.ptit.group4.shoppingweb.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.ORDER)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewMyOrders(Principal principal, Model model) {
        if (principal == null) {
            System.out.println("OrderController - Principal is null");
            return "redirect:/login";
        }

        System.out.println("OrderController - Principal name: " + principal.getName());

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            System.out.println("OrderController - User not found for: " + principal.getName());
            return "redirect:/login";
        }

        List<Order> orders = orderService.findOrdersByUser(user);
        model.addAttribute("orders", orders);

        return "orders/my-orders";
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(Principal principal,
                                   @PathVariable("id") Long orderId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.findById(orderId)
                .orElse(null);

        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/order";
        }

        // Kiểm tra quyền xem đơn hàng
        if (!order.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xem đơn hàng này");
            return "redirect:/order";
        }

        model.addAttribute("order", order);

        return "orders/order-details";
    }

    @GetMapping("/cancel/{id}")
    public String cancelOrder(Principal principal,
                              @PathVariable("id") Long orderId,
                              RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.findById(orderId)
                .orElse(null);

        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/order";
        }

        // Kiểm tra quyền hủy đơn hàng
        if (!order.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền hủy đơn hàng này");
            return "redirect:/order";
        }

        // Chỉ hủy được đơn hàng đang ở trạng thái PENDING
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            redirectAttributes.addFlashAttribute("error",
                    "Đơn hàng không thể hủy do đã được xử lý. Vui lòng liên hệ với chúng tôi để được hỗ trợ.");
            return "redirect:/order/" + orderId;
        }

        // Hủy đơn hàng
        orderService.updateOrderStatus(order, Order.OrderStatus.CANCELED);

        redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được hủy thành công");
        return "redirect:/order/" + orderId;
    }
}
