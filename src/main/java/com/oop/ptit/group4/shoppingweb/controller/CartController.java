package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.constants.Pages;
import com.oop.ptit.group4.shoppingweb.constants.PathConstants;
import com.oop.ptit.group4.shoppingweb.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.CART)
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String getCart(Model model) {
        model.addAttribute("products", cartService.getProductsInCart());
        return Pages.CART;
    }

    @PostMapping("/add")
    public String addProductToCart(@RequestParam("productId") Long productId) {
        cartService.addProductToCart(productId);
        return "redirect:" + PathConstants.CART;
    }

    @PostMapping("/remove")
    public String removeProductFromCart(@RequestParam("productId") Long productId) {
        cartService.removeProductFromCart(productId);
        return "redirect:" + PathConstants.CART;
    }
}
