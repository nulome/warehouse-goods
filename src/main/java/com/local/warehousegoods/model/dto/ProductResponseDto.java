package com.local.warehousegoods.model.dto;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Integer article;
    private String title;
    private String description;
    private String category;
    private Double price;
    private Integer quantity;
}
