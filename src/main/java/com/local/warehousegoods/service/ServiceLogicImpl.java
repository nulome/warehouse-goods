package com.local.warehousegoods.service;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.model.dto.*;
import com.local.warehousegoods.related.CustomExceptionValid;
import com.local.warehousegoods.related.UnknownValueException;
import com.local.warehousegoods.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceLogicImpl implements ServiceLogic {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    public UUID createProduct(CreateProductRequestDto createProductDto) {
        log.info("Получен запрос Post /product - article:{}", createProductDto.getArticle());
        checkProductByArticleInDataBase(createProductDto.getArticle());
        ProductDto productDto = modelMapper.map(createProductDto, ProductDto.class);
        productDto.setDateCreate(LocalDate.now());

        Product product = modelMapper.map(productDto, Product.class);
        product = productRepository.save(product);
        return product.getId();
    }

    @Override
    public ProductResponseDto updateProduct(ProductRequestDto productRequestDto) {
        log.info("Получен запрос Put /product - article:{}", productRequestDto.getArticle());
        ProductDto productDto = modelMapper.map(productRequestDto, ProductDto.class);

        Product checkProduct = checkProductByUuidInDataBase(productDto.getId());
        checkProductWithDataBase(productDto, checkProduct);
        checkDateUpdateByProductDto(productDto, checkProduct);

        Product product = modelMapper.map(productDto, Product.class);
        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto getProduct(UUID uuid) {
        log.trace("Получен запрос Get /product - uuid:{}", uuid);
        Product product = checkProductByUuidInDataBase(uuid);
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public UUID deleteProduct(UUID uuid) {
        log.trace("Получен запрос Delete /product - uuid:{}", uuid);
        checkProductByUuidInDataBase(uuid);
        productRepository.deleteById(uuid);
        return uuid;
    }

    @Override
    public Page<ProductResponseDto> getProductAll(PageRequest pageRequest) {
        log.trace("Получен запрос Get /product - all");
        Page<Product> pageList = productRepository.findAll(pageRequest);
        return pageList.map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    private void checkDateUpdateByProductDto(ProductDto productDto, Product checkProduct) {
        if ((int) productDto.getQuantity() != checkProduct.getQuantity()) {
            productDto.setDataUpdate(LocalDateTime.now());
        }
        if (productDto.getDataUpdate() == null && checkProduct.getDataUpdate() != null) {
            productDto.setDataUpdate(checkProduct.getDataUpdate());
        }
    }

    private void checkProductWithDataBase(ProductDto productDto, Product checkProduct) {
        if ((int) productDto.getArticle() != checkProduct.getArticle()) {
            log.warn("Ошибка при обновлении. Не корректный Article:{}", productDto.getArticle());
            throw new CustomExceptionValid("Не корректный Article товара.");
        }
        if (checkProduct.getDateCreate() == null) {
            log.warn("Ошибка при обновлении - article:{}. Отсутствует дата создания.", checkProduct.getArticle());
            throw new CustomExceptionValid("Отсутствует дата создания");
        } else {
            productDto.setDateCreate(checkProduct.getDateCreate());
        }
//        if (checkProduct.getVersion() == null) {
//            log.warn("Ошибка при обновлении - article:{}. Отсутствует версия.", checkProduct.getArticle());
//            throw new CustomExceptionValid("Отсутствует версия");
//        } else {
//            productDto.setVersion(checkProduct.getVersion());
//        }
    }

    private Product checkProductByUuidInDataBase(UUID uuid) {
        Optional<Product> checkProduct = productRepository.findById(uuid);
        if (checkProduct.isEmpty()) {
            log.warn("Неизвестный - uuid:{}", uuid);
            throw new UnknownValueException("Неизвестный uuid: " + uuid);
        }
        return checkProduct.get();
    }
    private void checkProductByArticleInDataBase(Integer article) {
        Optional<Product> checkProduct = productRepository.findByArticle(article);
        if (checkProduct.isPresent()) {
            log.warn("Уникальное значение используется - article:{}", article);
            throw new UnknownValueException("Уникальное значение используется - article: " + article);
        }
    }
}
