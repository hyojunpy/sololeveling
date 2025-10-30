package com.example.sololeveling.domain.product.repository;

import com.example.sololeveling.domain.product.entity.Product;
import com.example.sololeveling.domain.product.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
           select p
           from Product p
           order by case when p.type = :type then 0 else 1 end, p.id desc
           """)
    Page<Product> findAllOrderByPreferredType(@Param("type") ProductType type, Pageable pageable);

    Page<Product> findAllByOrderByTypeAscIdDesc(Pageable pageable);

    boolean existsByNameIgnoreCase(String productName);

}
