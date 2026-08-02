package com.vulneye.platform.service.interfaces;

import com.vulneye.platform.dto.common.PageResponse;
import com.vulneye.platform.dto.scan.CreateScanRequest;
import com.vulneye.platform.dto.scan.ScanDetailsResponse;
import com.vulneye.platform.dto.scan.ScanResponse;
import com.vulneye.platform.dto.scan.UpdateScanStatusRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScanService {

    ScanResponse createScan(CreateScanRequest request);

    PageResponse<ScanResponse> getAllScans(Pageable pageable);

    ScanResponse getScanById(Long id);

    List<ScanResponse> getScansByAsset(Long assetId);

    ScanResponse updateScanStatus(
            Long id,
            UpdateScanStatusRequest request);

    ScanDetailsResponse getScanDetails(Long scanId);

    void deleteScan(Long id);
}