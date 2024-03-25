package com.local.warehousegoods.service;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.related.CustomExceptionValid;
import com.local.warehousegoods.related.UnknownValueException;
import com.local.warehousegoods.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Logic implements ServiceLogic {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        log.info("Получен запрос Post /product - article:{}", product.getArticle());
        product.setDateCreate(LocalDate.now());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        log.info("Получен запрос Put /product - article:{}", product.getArticle());
        Product checkProduct = checkAvailabilityProduct(product.getArticle());
        checkProductFields(checkProduct, product);
        product.setDataUpdate(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Integer article) {
        log.trace("Получен запрос Get /product - article:{}", article);
        return checkAvailabilityProduct(article);
    }

    @Override
    public Product deleteProduct(Integer article) {
        log.trace("Получен запрос Delete /product - article:{}", article);
        Product product = checkAvailabilityProduct(article);
        productRepository.deleteById(product.getId());
        return product;
    }

    @Override
    public List<Product> getProductAll() {
        log.trace("Получен запрос Get /product - all");
        return productRepository.findAll();
    }

    private void checkProductFields(Product checkProduct, Product product){
        if (product.getDateCreate() == null) {
            log.warn("Ошибка при обновлении - {}", product.getArticle());
            throw new CustomExceptionValid("Отсутствует дата создания");
        }
        if (product.getId() == null || product.getId() != checkProduct.getId()) {
            product.setId(checkProduct.getId());
        }
    }
    private Product checkAvailabilityProduct(Integer article) {
        Product checkProduct = productRepository.findByarticle(article);
        if (checkProduct == null) {
            log.warn("Неизвестный - article: {}", article);
            throw new UnknownValueException("Неизвестный артикул: " + article);
        }
        return checkProduct;
    }
}
