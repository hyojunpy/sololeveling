package com.example.sololeveling.domain.asset.dto;

import com.example.sololeveling.domain.asset.entity.Asset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AssetResponseDto {
    private String assetName;
    private String assetType;
    private BigDecimal assetAmount;

    public static AssetResponseDto from(Asset asset) {
        return new AssetResponseDto(
                asset.getName(),
                asset.getType(),
                asset.getAmount()
        );
    }

}
