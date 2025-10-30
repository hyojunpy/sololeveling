package com.example.sololeveling.domain.product.dto;

import com.example.sololeveling.domain.product.entity.Product;
import com.example.sololeveling.domain.product.entity.ProductType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class ProductResponseDto {
    private final Long id;
    private final String productName;
    private final ProductType productType;
    private final BigDecimal interestRate;
    private final int durationMonths;
    private final String productDescription;

    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getType(),
                product.getInterestRate(),
                product.getDurationMonths(),
                product.getDescription()
        );
    }
}
