package com.local.warehousegoods.service;

import com.local.warehousegoods.model.dto.CreateProductRequestDto;
import com.local.warehousegoods.model.dto.ProductRequestDto;
import com.local.warehousegoods.model.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface ServiceLogic {
    UUID createProduct(CreateProductRequestDto createProductDto);

    ProductResponseDto updateProduct(ProductRequestDto productRequestDto);

    ProductResponseDto getProduct(UUID uuid);

    UUID deleteProduct(UUID uuid);

    Page<ProductResponseDto> getProductAll(PageRequest pageRequest);
}
