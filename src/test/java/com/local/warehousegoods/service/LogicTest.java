package com.local.warehousegoods.service;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.related.CustomExceptionValid;
import com.local.warehousegoods.related.UnknownValueException;
import com.local.warehousegoods.repository.ProductRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LogicTest {

    ServiceLogic serviceLogic;
    @Autowired
    ProductRepository productRepository;
    Product product1;
    Product product2;
    Product product3;
    Validator validator;

    @BeforeEach
    void create(){
        serviceLogic = new Logic(productRepository);
        product1 = Product.builder()
                .article(111)
                .title("Товар1")
                .description("Описание1")
                .category("Категория1")
                .price(500)
                .quantity(1)
                .build();

        product2 = Product.builder()
                .article(222)
                .title("Товар1")
                .description("Описание1")
                .category("Категория1")
                .price(500)
                .quantity(1)
                .build();

        product3 = Product.builder()
                .article(333)
                .title("Товар1")
                .description("Описание1")
                .category("Категория1")
                .price(500)
                .quantity(1)
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void checkTestCreateProductValidation(){
        serviceLogic.createProduct(product1);
        assertEquals(LocalDate.now(), product1.getDateCreate(), "Не вносится дата создания");
        assertNotNull(product1.getId(), "Не заполняется UUID");

        serviceLogic.updateProduct(product1);
        assertNotNull(product1.getDataUpdate(), "Не вносится дата обновления");

        assertThrows(UnknownValueException.class, () -> {
            serviceLogic.updateProduct(product2);
        }, "Неизвестный артикул. Ожидалось исключение UnknownValueException.");


        product1.setDateCreate(null);
        assertThrows(CustomExceptionValid.class, () -> {
            serviceLogic.updateProduct(product1);
        }, "Обновление товара с неизвестной датой создания. Ожидалось исключение CustomExceptionValid.");
        product1.setDateCreate(LocalDate.now());

        serviceLogic.createProduct(product2);
        serviceLogic.createProduct(product3);
        assertEquals(3, serviceLogic.getProductAll().size(), "Неверное количество добавленных товаров");


        serviceLogic.deleteProduct(product3.getArticle());
        assertEquals(2, serviceLogic.getProductAll().size(), "После удаления кол-во товаров не изменилось");
        assertThrows(UnknownValueException.class, () -> {
            serviceLogic.getProduct(product3.getArticle());
        }, "Товар не удален. Ожидалось исключение UnknownValueException.");

        product1.setArticle(-1);
        Set<ConstraintViolation<Product>> violations = validator.validate(product1);
        assertFalse(violations.isEmpty(), "Артикул должен быть положительный");

        product1.setArticle(111);
        product1.setCategory("  ");
        violations = validator.validate(product1);
        assertFalse(violations.isEmpty(), "Категория должна быть указана");
    }
}