package com.example.sololeveling.domain.product.controller;

import com.example.sololeveling.domain.product.dto.ProductRequestDto;
import com.example.sololeveling.domain.product.dto.ProductResponseDto;
import com.example.sololeveling.domain.product.service.ProductService;
import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ProductRequestDto requestDto
    ) throws AccessDeniedException {
        User user = userDetails.getUser();
        ProductResponseDto responseDto = productService.createProduct(user, requestDto);
        URI location = URI.create("/api/products/" + responseDto.getId());

        return ResponseEntity.created(location).body(responseDto);
    }

    //성향에 따른 추천 상품 리스트 확인
    @PostMapping("/find")
    public ResponseEntity<Page<ProductResponseDto>> findProductByType(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String type){
        try {
            Page<ProductResponseDto> responseDtos = productService.findByUserType(type, pageable);
            return ResponseEntity.ok(responseDtos);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    //추천 상품 상세 확인
    @PostMapping("/find-one")
    public ResponseEntity<ProductResponseDto> findProductDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long id
    ){
        ProductResponseDto responseDto = productService.findById(id);
        return ResponseEntity.ok(responseDto);
    }
}
