package com.local.warehousegoods.storage;

import com.local.warehousegoods.model.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Memory implements ProductStorage {

    HashMap<Integer, Product> productHashMap = new HashMap<>();

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
