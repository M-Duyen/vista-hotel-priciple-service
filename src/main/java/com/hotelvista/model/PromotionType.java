package com.hotelvista.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionType {
    @Id
    @Column(name = "promotion_type_id", nullable = false, length = 50)
    private String promotionTypeId;

    @Column(name = "promotion_type_name", nullable = false, length = 255)
    private String promotionTypeName;

    @Column(length = 1000)
    private String description;
}

