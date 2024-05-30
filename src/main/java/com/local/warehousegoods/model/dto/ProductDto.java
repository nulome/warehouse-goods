package com.local.warehousegoods.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;
    private Integer article;
    private String title;
    private String description;
    private String category;
    private Double price;
    private Integer quantity;
    private LocalDate dateCreate;
    private LocalDateTime dataUpdate;
//    private Integer version;
}
