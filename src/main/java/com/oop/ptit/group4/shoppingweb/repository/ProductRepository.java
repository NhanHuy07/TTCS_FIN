package com.oop.ptit.group4.shoppingweb.repository;

import com.oop.ptit.group4.shoppingweb.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIdIn(List<Long> productsIds);

    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);

    @Query("SELECT product FROM Product product WHERE " +
            "(CASE " +
            "   WHEN :searchType = 'productTitle' THEN UPPER(product.productTitle) " +
            "   WHEN :searchType = 'country' THEN UPPER(product.country) " +
            "   ELSE UPPER(product.brand) " +
            "END) " +
            "LIKE UPPER(CONCAT('%',:text,'%')) " +
            "ORDER BY product.price ASC")
    Page<Product> searchProducts(String searchType, String text, Pageable pageable);

    @Query("SELECT product FROM Product product " +
            "WHERE (coalesce(:brands, null) IS NULL OR product.brand IN :brands) " +
            "AND (coalesce(:colors, null) IS NULL OR product.productColor IN :colors) " +
            "AND (coalesce(:priceStart, null) IS NULL OR product.price BETWEEN :priceStart AND :priceEnd) " +
            "ORDER BY product.price ASC")
    Page<Product> getProductsByFilterParams(
            List<String> brands,
            List<String> colors,
            Integer priceStart,
            Integer priceEnd,
            Pageable pageable);
}
