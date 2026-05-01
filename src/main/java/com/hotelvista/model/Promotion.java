package com.hotelvista.model;

import com.hotelvista.model.enums.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    @Column(name = "promotion_id", nullable = false, length = 50)
    private String promotionId;

    @Column(name = "promotion_name", nullable = false, length = 255)
    private String promotionName;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 30)
    private DiscountType discountType;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "admin_id", length = 50)
    private String adminId;

    @Column(name = "promotion_type_id", nullable = false, length = 50)
    private String promotionTypeId;
}

