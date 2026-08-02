package com.vulneye.platform.repository;

import com.vulneye.platform.entity.Asset;
import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.entity.enums.ScanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanRepository extends JpaRepository<Scan, Long> {

    Page<Scan> findAll(Pageable pageable);

    Page<Scan> findByAsset(Asset asset, Pageable pageable);

    Page<Scan> findByStatus(ScanStatus status, Pageable pageable);

    Page<Scan> findByAssetAndStatus(
            Asset asset,
            ScanStatus status,
            Pageable pageable);

    List<Scan> findByAsset(Asset asset);

    List<Scan> findByStatus(ScanStatus status);

    List<Scan> findByAssetAndStatus(
            Asset asset,
            ScanStatus status);
}