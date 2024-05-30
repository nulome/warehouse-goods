package com.local.warehousegoods.controller;

import com.local.warehousegoods.model.dto.CreateProductRequestDto;
import com.local.warehousegoods.model.dto.ProductRequestDto;
import com.local.warehousegoods.model.dto.ProductResponseDto;
import com.local.warehousegoods.service.ServiceLogic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "controller")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ServiceLogic serviceLogic;

    @Operation(summary = "Создание нового товара", description = "Собирает модель товара и сохраняет в базе. " +
            "Возвращает UUID добавленного продукта.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UUID create(@RequestBody @Valid CreateProductRequestDto createProductRequest) {
        return serviceLogic.createProduct(createProductRequest);
    }

    @Operation(summary = "Получение всех товаров", description = "Возвращает базу сохраненных товаров")
    @GetMapping
    Page<ProductResponseDto> getProductAll(@RequestParam(value = "page-number", defaultValue = "0") @Min(0) int number,
                                           @RequestParam(value = "page-size", defaultValue = "10") @Min(3) @Max(50) int size) {
        return serviceLogic.getProductAll(PageRequest.of(number, size));
    }

    @Operation(summary = "Получение выбранного товара", description = "Возвращает имеющийся товар по артикул")
    @GetMapping("/{uuid}")
    ProductResponseDto getProduct(@PathVariable UUID uuid) {
        return serviceLogic.getProduct(uuid);
    }

    @Operation(summary = "Обновление товара", description = "Обновляет и возвращает обновленный товар")
    @PutMapping
    ProductResponseDto updateProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        return serviceLogic.updateProduct(productRequestDto);
    }

    @Operation(summary = "Удаление товара", description = "Удаляет из базы товар по артикул. " +
            "Возвращает UUID удаленного продукта.")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    UUID deleteProduct(@PathVariable UUID uuid) {
        return serviceLogic.deleteProduct(uuid);
    }
}
