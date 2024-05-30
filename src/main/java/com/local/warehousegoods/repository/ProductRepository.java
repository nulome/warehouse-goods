package com.local.warehousegoods.repository;

import com.local.warehousegoods.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByArticle(Integer article);


    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Product> findAll();
}
