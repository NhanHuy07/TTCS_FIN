package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.constants.Pages;
import com.oop.ptit.group4.shoppingweb.constants.PathConstants;
import com.oop.ptit.group4.shoppingweb.dto.request.ProductRequest;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import com.oop.ptit.group4.shoppingweb.dto.response.UserInfoResponse;
import com.oop.ptit.group4.shoppingweb.service.AdminService;
import com.oop.ptit.group4.shoppingweb.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(PathConstants.ADMIN)
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/products")
    public String getProducts(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getProducts(pageable));
        return Pages.ADMIN_LAPTOPS;
    }

    @GetMapping("/products/search")
    public String searchProducts(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchProducts(request, pageable));
        return Pages.ADMIN_LAPTOPS;
    }

    @GetMapping("/users")
    public String getUsers(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getUsers(pageable));
        return Pages.ADMIN_ALL_USERS;
    }

    @GetMapping("/users/search")
    public String searchUsers(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchUsers(request, pageable));
        return Pages.ADMIN_ALL_USERS;
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", adminService.getOrder(orderId));
        return Pages.ORDER;
    }

    @GetMapping("/orders")
    public String getOrders(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getOrders(pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/orders/search")
    public String searchOrders(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchOrders(request, pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/product/{productId}") // doi ten sau
    public String getProduct(@PathVariable Long productId, Model model) {
        model.addAttribute("product", adminService.getProductById(productId));
        return Pages.ADMIN_EDIT_LAPTOP;
    }

    @PostMapping("/edit/product")
    public String editProduct(@Valid ProductRequest product, BindingResult bindingResult, Model model,
                              @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (controllerUtils.validateInputFields(bindingResult, model, "product", product)) {
            return Pages.ADMIN_EDIT_LAPTOP;
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/products", adminService.editProduct(product, file));
    }

    @GetMapping("/add/product")
    public String addProduct() {
        return Pages.ADMIN_ADD_LAPTOP;
    }

    @PostMapping("/add/product")
    public String addProduct(@Valid ProductRequest product, BindingResult bindingResult, Model model,
                             @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (controllerUtils.validateInputFields(bindingResult, model, "product", product)) {
            return Pages.ADMIN_ADD_LAPTOP;
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/products", adminService.addProduct(product, file));
    }

    @GetMapping("/user/{userId}")
    public String getUserById(@PathVariable Long userId, Model model, Pageable pageable) {
        UserInfoResponse userResponse = adminService.getUserById(userId, pageable);
        model.addAttribute("user", userResponse.getUser());
        controllerUtils.addPagination(model, userResponse.getOrders());
        return Pages.ADMIN_USER_DETAIL;
    }
}
