package com.oop.ptit.group4.shoppingweb.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SearchRequest {
    private List<String> brands;
    private List<String> colors;
    private BigDecimal price = BigDecimal.ZERO;
    private String searchType;
    private String text;
}

