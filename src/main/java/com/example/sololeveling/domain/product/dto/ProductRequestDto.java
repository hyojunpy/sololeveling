package com.example.sololeveling.domain.product.dto;

import com.example.sololeveling.domain.product.entity.ProductType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class ProductRequestDto {
    private String productName;
    private ProductType productType;
    private BigDecimal productInterestRate;
    private Integer productDurationMonths;
    private String productDescription;
}
