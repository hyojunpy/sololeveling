package com.example.sololeveling.domain.asset.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class AssetRequestDto {
    private String assetName;
    private String assetType;
    private BigDecimal assetAmount;
}
