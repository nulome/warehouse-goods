package com.local.warehousegoods.related;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@ConditionalOnExpression(
        "'${spring.profiles.active}'.equals('test') or '${spring.profiles.active}'.equals('local') " +
                "or '${app.scheduling.priceCreateInData}'")
public class InitiateUtils implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final static int INCREMENT_COUNT_ENTITY = 10;

    @TrackingExecutionTimer
    @Override
    public void run(String... args) throws Exception {
        System.out.println("run database initialization");
        createDataBaseProductEntity();
    }

    private void createDataBaseProductEntity(){

        for(int i = 1; i <= INCREMENT_COUNT_ENTITY; i++){
            Product product = Product.builder()
                    .article(i)
                    .title("Title")
                    .description("desc")
                    .category("cat")
                    .price(100.0)
                    .quantity(50)
                    .dateCreate(LocalDate.now())
                    .build();

            productRepository.save(product);
        }
    }
}