package com.vulneye.platform.service.interfaces;

import com.vulneye.platform.dto.scan.CreateScanRequest;
import com.vulneye.platform.dto.scan.ScanResponse;
import com.vulneye.platform.dto.scan.UpdateScanStatusRequest;

import java.util.List;

public interface ScanService {

    ScanResponse createScan(CreateScanRequest request);

    List<ScanResponse> getAllScans();

    ScanResponse getScanById(Long id);

    List<ScanResponse> getScansByAsset(Long assetId);

    ScanResponse updateScanStatus(Long id, UpdateScanStatusRequest request);

    void deleteScan(Long id);

}