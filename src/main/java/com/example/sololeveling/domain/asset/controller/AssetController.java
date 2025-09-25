package com.example.sololeveling.domain.asset.controller;


import com.example.sololeveling.domain.asset.dto.AssetRequestDto;
import com.example.sololeveling.domain.asset.dto.AssetResponseDto;
import com.example.sololeveling.domain.asset.service.AssetService;
import com.example.sololeveling.domain.user.dto.UserResponseDto;
import com.example.sololeveling.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
@Validated
public class AssetController {
    private final AssetService assetService;

    @PostMapping("/find")        //자산 목록 조회
    public ResponseEntity<Page<AssetResponseDto>> findAsset(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<AssetResponseDto> page = assetService.findAll(userDetails.getUser(), pageable).map(AssetResponseDto::from);

        return ResponseEntity.ok(page);
    }

    @PostMapping     //자산 생성
    public ResponseEntity<AssetResponseDto> createAsset(
            @Valid @RequestBody AssetRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AssetResponseDto assetResponseDto = assetService.create(requestDto, userDetails.getUser());
        return ResponseEntity.ok(assetResponseDto);
    }


    @PostMapping("/edit/{id}")   //자산 업데이트
    public ResponseEntity<AssetResponseDto> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return assetService.update(id, requestDto, userDetails.getUser())
                .map(AssetResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/remove/{id}") //자산 삭제
    public void deleteAsset(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        assetService.delete(id, userDetails.getUser());
    }

}
