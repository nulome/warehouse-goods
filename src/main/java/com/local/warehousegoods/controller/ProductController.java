package com.local.warehousegoods.controller;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.related.CreateValid;
import com.local.warehousegoods.related.UpdateValid;
import com.local.warehousegoods.service.ServiceLogic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "controller")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ServiceLogic logic;

    @Operation(summary = "Создание нового товара", description = "Собирает модель товара и сохраняет в базе")
    @Validated({CreateValid.class})
    @PostMapping
    Product create(@RequestBody @Valid Product product) {
        return logic.createProduct(product);
    }

    @Operation(summary = "Получение всех товаров", description = "Возвращает базу сохраненных товаров")
    @GetMapping
    List<Product> getProductAll() {
        return logic.getProductAll();
    }

    @Operation(summary = "Получение выбранного товара", description = "Возвращает имеющийся товар по артикул")
    @GetMapping("/{article}")
    Product getProduct(@PathVariable Integer article) {
        return logic.getProduct(article);
    }

    @Operation(summary = "Обновление товара", description = "Обновляет и возвращает обновленный товар")
    @Validated({UpdateValid.class})
    @PutMapping
    Product updateProduct(@RequestBody @Valid Product product) {
        return logic.updateProduct(product);
    }

    @Operation(summary = "Удаление товара", description = "Удаляет из базы товар по артикул")
    @DeleteMapping("/{article}")
    Product deleteProduct(@PathVariable Integer article) {
        return logic.deleteProduct(article);
    }
}
