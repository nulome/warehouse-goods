package com.local.warehousegoods.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductRequestDto {
    @Positive
    private Integer article;
    @NotBlank
    private String title;
    @Size(max = 100)
    private String description;
    @NotBlank
    private String category;
    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Integer quantity;
}
