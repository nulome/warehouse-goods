package com.local.warehousegoods.service;

import com.local.warehousegoods.model.Product;

public interface ServiceLogic {
    Product createProduct(Product product);

    Product updateProduct(Product product);

    Product getProduct(Product product);

    void deleteProduct(Product product);
}
