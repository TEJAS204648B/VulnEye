package com.vulneye.platform.repository;

import com.vulneye.platform.entity.Finding;
import com.vulneye.platform.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FindingRepository extends JpaRepository<Finding, Long> {

    List<Finding> findByScanId(Long scanId);

    List<Finding> findByScan(Scan scan);

    Optional<Finding> findByScanAndHostAddressAndPortAndProtocol(
            Scan scan,
            String hostAddress,
            Integer port,
            String protocol);

}