package com.local.warehousegoods.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "article")
})
public class Product {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private Integer article;
    private String title;
    private String description;
    private String category;
    private Double price;
    private Integer quantity;
    @Column(name = "date_create")
    private LocalDate dateCreate;
    @Column(name = "date_update")
    private LocalDateTime dataUpdate;
    //@Version
//    private Integer version;
}
