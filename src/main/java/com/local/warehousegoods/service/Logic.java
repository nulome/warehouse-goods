package com.local.warehousegoods.service;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.storage.ProductStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Logic implements ServiceLogic {

    @Autowired
    ProductStorage memory;

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }

    @Override
    public Product getProduct(Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Product product) {

    }
}
