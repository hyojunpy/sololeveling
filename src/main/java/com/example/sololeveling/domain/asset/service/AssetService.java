package com.example.sololeveling.domain.asset.service;

import com.example.sololeveling.domain.asset.dto.AssetRequestDto;
import com.example.sololeveling.domain.asset.dto.AssetResponseDto;
import com.example.sololeveling.domain.asset.entity.Asset;
import com.example.sololeveling.domain.asset.repository.AssetRepository;
import com.example.sololeveling.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    //자산 조회
    @Transactional(readOnly = true)
    public Page<Asset> findAll(User user, Pageable pageable) {
        return assetRepository.findAllByUser(user, pageable);
    }


    @Transactional
    public AssetResponseDto create(@Valid AssetRequestDto requestDto, User user) {
        Asset asset = new Asset(
                requestDto.getAssetType(),
                requestDto.getAssetName(),
                requestDto.getAssetAmount(),
                'N',
                user
        );
        asset = assetRepository.save(asset);
        return AssetResponseDto.from(asset);
    }

    @Transactional
    public Optional<Asset> update(Long assetId, @Valid AssetRequestDto requestDto, User user) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));

        // 소유자 검증
        if (!asset.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this asset.");
        }

        return assetRepository.findById(assetId).map(existing -> {
            existing.update(
                    requestDto.getAssetType(),
                    requestDto.getAssetName(),
                    requestDto.getAssetAmount()
            );
            return assetRepository.save(existing);
        });
    }

    @Transactional
    public void delete(Long Id, User user) {
        Asset asset = assetRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));

        // 소유자 검증
        if (!asset.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this asset.");
        }
        asset.softDelete();
        assetRepository.save(asset);
    }

}
