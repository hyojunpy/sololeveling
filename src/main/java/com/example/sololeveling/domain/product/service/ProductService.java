package com.example.sololeveling.domain.product.service;

import com.example.sololeveling.domain.product.dto.ProductRequestDto;
import com.example.sololeveling.domain.product.dto.ProductResponseDto;
import com.example.sololeveling.domain.product.entity.Product;
import com.example.sololeveling.domain.product.entity.ProductType;
import com.example.sololeveling.domain.product.repository.ProductRepository;
import com.example.sololeveling.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    @Transactional
    public ProductResponseDto createProduct(User user, @Valid ProductRequestDto requestDto) throws AccessDeniedException {
        //추후 권한  추가
//        if(user.getRole() != Role.ROLE_ADMIN) {
//            throw new AccessDeniedException("관리자만 접근할 수 있습니다.");
//        }
        String name = requestDto.getProductName().trim();

        if (productRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("이미 존재하는 상품명입니다: " + name);
        }


        Product product = new Product(
                requestDto.getProductName(),
                requestDto.getProductType(),
                requestDto.getProductInterestRate(),
                requestDto.getProductDurationMonths(),
                requestDto.getProductDescription()
        );

        return ProductResponseDto.from(productRepository.save(product));
    }

    // 유저 성향에 따른 상품 리스트 검색
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findByUserType(String type, Pageable pageable) {
        if (!StringUtils.hasText(type)) {
            // type 미입력 → type별로 묶어서 정렬 (type asc, id desc)
            return productRepository.findAllByOrderByTypeAscIdDesc(pageable)
                    .map(ProductResponseDto::from);
        }

        ProductType target = ProductType.from(type);
        return productRepository.findAllOrderByPreferredType(target, pageable)
                .map(ProductResponseDto::from);

    }

    //상품 상세 조회 (설명을 따로 빼야겠다..)
    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {
        return ProductResponseDto.from(productRepository.findById(id).get());
    }
}
