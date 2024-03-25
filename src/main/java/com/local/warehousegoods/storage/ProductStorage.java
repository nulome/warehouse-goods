package com.local.warehousegoods.storage;

import com.local.warehousegoods.model.Product;

public interface ProductStorage {
    Product createProduct(Product product);

    Product updateProduct(Product product);

    Product getProduct(Product product);

    void deleteProduct(Product product);
}
