package com.local.warehousegoods.service;

import com.local.warehousegoods.model.Product;

import java.util.List;

public interface ServiceLogic {
    Product createProduct(Product product);

    Product updateProduct(Product product);

    Product getProduct(Integer article);

    Product deleteProduct(Integer article);

    List<Product> getProductAll();
}
