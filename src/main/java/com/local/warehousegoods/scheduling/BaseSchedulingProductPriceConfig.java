package com.local.warehousegoods.scheduling;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.related.TrackingExecutionTimer;
import com.local.warehousegoods.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@ConditionalOnProperty(
        value = "app.scheduling.enabled",
        havingValue = "true",
        matchIfMissing = false)
@ConditionalOnExpression(
        "!'${spring.profiles.active}'.equals('local')")
@ConditionalOnMissingBean(OptimizedSchedulingProductPriceConfig.class)
public class BaseSchedulingProductPriceConfig {

    final double priceIncreasePercentage;
    final double PERCENT_100 = 100;
    final int DEFAULT_PAGE_NUMBER = 0;
    final int DEFAULT_PAGE_SIZE = 50;

    @Autowired
    private ProductRepository productRepository;

    public BaseSchedulingProductPriceConfig(@Value("${app.scheduling.priceIncreasePercentage:1.0}") double priceIncreasePercentage) {
        this.priceIncreasePercentage = priceIncreasePercentage;
    }

    @Scheduled(initialDelay = 1)
    public void print() {
        System.out.println("****    Run Base Scheduling    ****");
    }


    @TrackingExecutionTimer
    @Transactional
    @Scheduled(fixedDelayString = "${app.scheduling.period:10000}", initialDelay = 5000)
    public void schedulePriceProductIncrease() {
        Optional<Product> product = productRepository.findByArticle(1);
        product.ifPresent(value -> System.out.println("Start Increase Price With Model Product. Start Price: " + value.getPrice()));

        incrementPricePercentageAndSave(productRepository.findAll());
        System.out.println("Saving to the database.");
        product = productRepository.findByArticle(1);
        product.ifPresent(value -> System.out.println("End Price: " + value.getPrice()));
        System.out.println("Finish Increase Price Base Scheduling.");
    }

    private void saveAllProduct(List<Product> listProduct) {
        productRepository.saveAll(listProduct);
    }

    private void incrementPricePercentageAndSave(List<Product> listProduct) {
        List<Product> list = listProduct.stream()
                .map(this::updPrice)
                .collect(Collectors.toList());
        saveAllProduct(list);
    }

    private Product updPrice(Product product) {
        double priceDouble = product.getPrice();
        int incPrice = (int) (priceDouble * priceIncreasePercentage * PERCENT_100);
        product.setPrice(((double) incPrice) / PERCENT_100 / PERCENT_100 + priceDouble);
        return product;
    }

    /*public void schedulePriceProductIncrease() {
        System.out.println("Start Increase Price With Model Product");
        Page<Product> listProduct = productRepository.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
        int totalPage = listProduct.getTotalPages();
        incrementPricePercentageAndSave(listProduct);
        for (int i = 1; i < totalPage; i++) {
            incrementPricePercentageAndSave(productRepository.findAll(PageRequest.of(i, DEFAULT_PAGE_SIZE)));
        }
        System.out.println("We are waiting for the execution time.");
    }*/
}
