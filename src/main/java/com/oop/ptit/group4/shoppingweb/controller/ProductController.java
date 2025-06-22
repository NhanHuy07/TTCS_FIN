package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.constants.Pages;
import com.oop.ptit.group4.shoppingweb.constants.PathConstants;
import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import com.oop.ptit.group4.shoppingweb.service.CartService;
import com.oop.ptit.group4.shoppingweb.service.ProductService;
import com.oop.ptit.group4.shoppingweb.service.UserService;
import com.oop.ptit.group4.shoppingweb.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.LAPTOP)
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ControllerUtils controllerUtils;
    private final CartService cartService;

    @GetMapping("/{productId}")
    public String getProductById(@PathVariable Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        return Pages.LAPTOP;
    }

    @GetMapping
    public String getProductsByFilterParams(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, productService.getProductsByFilterParams(request, pageable));
        return Pages.LAPTOPS;
    }

    @GetMapping("/search")
    public String searchProducts(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, productService.searchProducts(request, pageable));
        return Pages.LAPTOPS;
    }

    @PostMapping("/{id}/add-to-cart")
    public String addToCart(@PathVariable Long id, @RequestParam("quantity") int quantity,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getAuthenticatedUser();
            Product product = productService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

            cartService.addToCart(user, product, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã thêm " + product.getProductTitle() + " vào giỏ hàng");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
            return "redirect:/product/" + id;
        }
    }
}
