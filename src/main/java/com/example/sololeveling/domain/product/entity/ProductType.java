package com.example.sololeveling.domain.product.entity;

public enum ProductType {
    //예금, 적금, 펀드
    DEPOSIT, SAVINGS, FUND;

    public static ProductType from(String raw) {
        if (raw == null) throw new IllegalArgumentException("type is null");
        try {
            return ProductType.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown type: " + raw);
        }
    }
}
