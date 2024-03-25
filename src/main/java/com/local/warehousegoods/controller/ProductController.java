package com.local.warehousegoods.controller;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.service.ServiceLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ServiceLogic logic;

    @PostMapping
    Product create(@RequestBody Product product){
        return logic.createProduct(product);
    }
}
