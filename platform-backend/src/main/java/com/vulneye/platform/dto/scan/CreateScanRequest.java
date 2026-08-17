package com.vulneye.platform.dto.scan;

import com.vulneye.platform.entity.enums.ScanType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateScanRequest {

    @NotNull(message = "Asset ID is required")
    @Positive(message = "Asset ID must be greater than 0")
    private Long assetId;

    @NotNull(message = "Scan type is required")
    private ScanType scanType;

    public CreateScanRequest() {
        // Required by Jackson for JSON deserialization.
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public ScanType getScanType() {
        return scanType;
    }

    public void setScanType(ScanType scanType) {
        this.scanType = scanType;
    }
}