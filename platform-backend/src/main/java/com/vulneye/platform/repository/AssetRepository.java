package com.vulneye.platform.repository;

import com.vulneye.platform.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByTarget(String target);

    boolean existsByTarget(String target);

}