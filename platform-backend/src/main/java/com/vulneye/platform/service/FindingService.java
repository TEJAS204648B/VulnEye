package com.vulneye.platform.service;

import com.vulneye.platform.dto.response.FindingResponse;
import com.vulneye.platform.entity.Finding;
import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.infrastructure.parser.dto.NmapScanResult;

import java.util.List;

public interface FindingService {

    void saveFindings(
            Scan scan,
            NmapScanResult scanResult);

    List<FindingResponse> getFindingsByScanId(Long scanId);

    List<Finding> findByScan(Scan scan);

}