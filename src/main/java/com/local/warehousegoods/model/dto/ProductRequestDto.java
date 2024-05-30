package com.local.warehousegoods.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductRequestDto {
    @NotNull
    private UUID id;
    @Positive
    private Integer article;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String category;
    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Integer quantity;
}
