package com.local.warehousegoods.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Product {
    Integer article;
    String title;
    String description;
    String category;
    Integer price;
    Integer quantity;
    LocalDate dateCreate;
    LocalDateTime dataUpdate;
}
