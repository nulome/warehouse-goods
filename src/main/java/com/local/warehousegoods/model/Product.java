package com.local.warehousegoods.model;

import com.local.warehousegoods.related.CreateValid;
import com.local.warehousegoods.related.UpdateValid;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "products", uniqueConstraints={
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "article")
}
)
public class Product {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    @Positive
    private Integer article;
    @NotBlank
    private String title;
    @Size(max = 100)
    private String description;
    @NotBlank
    private String category;
    @PositiveOrZero
    private Integer price;
    @PositiveOrZero
    private Integer quantity;
    @PastOrPresent
    private LocalDate dateCreate;
    @Null(groups = {CreateValid.class})
    @PastOrPresent(groups = {UpdateValid.class})
    private LocalDateTime dataUpdate;
}
