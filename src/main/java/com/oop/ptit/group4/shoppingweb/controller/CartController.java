package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.constants.Pages;
import com.oop.ptit.group4.shoppingweb.constants.PathConstants;
import com.oop.ptit.group4.shoppingweb.domain.Cart;
import com.oop.ptit.group4.shoppingweb.domain.CartItem;
import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.service.CartService;
import com.oop.ptit.group4.shoppingweb.service.ProductService;
import com.oop.ptit.group4.shoppingweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.CART)
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public String viewCart(Model model) {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.getOrCreateCart(user);
            BigDecimal total = cartService.getCartTotal(cart);

            model.addAttribute("cart", cart);
            model.addAttribute("total", total);
            return "cart/view";
        } catch (Exception e) {
            // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
            return "redirect:/login";
        }
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getAuthenticatedUser();
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng với ID: " + productId));

            // Kiểm tra số lượng có hợp lệ không
            if (quantity <= 0) {
                redirectAttributes.addFlashAttribute("error", "Số lượng phải lớn hơn 0");
                return "redirect:/product/" + productId;
            }

            // Kiểm tra số lượng có vượt quá tồn kho không
            if (quantity > product.getStock()) {
                redirectAttributes.addFlashAttribute("error", "Số lượng vượt quá tồn kho");
                return "redirect:/product/" + productId;
            }

            cartService.addToCart(user, product, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã thêm " + product.getProductTitle() + " vào giỏ hàng");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
            return "redirect:/product";
        }
    }

    @PostMapping("/update/{itemId}")
    public String updateCartItem(@PathVariable Long itemId, @RequestParam("quantity") int quantity,
                                 RedirectAttributes redirectAttributes) {
        try {
            CartItem item = cartService.updateCartItem(itemId, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật số lượng");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật giỏ hàng: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/remove/{itemId}")
    public String removeCartItem(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
        try {
            cartService.removeCartItem(itemId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm khỏi giỏ hàng: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getAuthenticatedUser();
            cartService.clearCart(user);
            redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa giỏ hàng: " + e.getMessage());
            return "redirect:/cart";
        }
    }
}
