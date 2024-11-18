package com.oop.ptit.group4.shoppingweb.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private List<String> brands;
    private List<String> colors;
    private Integer price = 0;
    private String searchType;
    private String text;
}
