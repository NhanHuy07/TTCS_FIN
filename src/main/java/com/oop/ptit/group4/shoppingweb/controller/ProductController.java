package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.constants.Pages;
import com.oop.ptit.group4.shoppingweb.constants.PathConstants;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import com.oop.ptit.group4.shoppingweb.service.ProductService;
import com.oop.ptit.group4.shoppingweb.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.LAPTOP)
public class ProductController {

    private final ProductService productService;
    private final ControllerUtils controllerUtils;

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
}
