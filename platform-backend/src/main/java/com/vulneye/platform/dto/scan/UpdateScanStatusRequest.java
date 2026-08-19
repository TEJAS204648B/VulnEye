package com.vulneye.platform.dto.scan;

import com.vulneye.platform.entity.enums.ScanStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateScanStatusRequest {

    @NotNull(message = "Scan status is required")
    private ScanStatus status;

    public UpdateScanStatusRequest() {
        // Required by Jackson for JSON deserialization.
    }

    public ScanStatus getStatus() {
        return status;
    }

    public void setStatus(ScanStatus status) {
        this.status = status;
    }
}