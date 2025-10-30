package com.example.sololeveling.domain.product.dto;

import com.example.sololeveling.domain.product.entity.ProductType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class ProductRequestDto {
    private final String productName;
    private final ProductType productType;
    private final BigDecimal productInterestRate;
    private final Integer productDurationMonths;
    private final String productDescription;
}
