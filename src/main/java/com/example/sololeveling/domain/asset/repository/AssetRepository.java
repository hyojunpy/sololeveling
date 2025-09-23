package com.example.sololeveling.domain.asset.repository;

import com.example.sololeveling.domain.asset.entity.Asset;
import com.example.sololeveling.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Page<Asset> findAllByUser(User user, Pageable pageable);
}
