package com.vulneye.platform.controller;

import com.vulneye.platform.dto.ApiResponse;
import com.vulneye.platform.dto.scan.CreateScanRequest;
import com.vulneye.platform.dto.scan.ScanResponse;
import com.vulneye.platform.dto.scan.UpdateScanStatusRequest;
import com.vulneye.platform.service.interfaces.ScanService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.vulneye.platform.dto.scan.ScanDetailsResponse;
import com.vulneye.platform.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

        private final ScanService scanService;

        public ScanController(ScanService scanService) {
                this.scanService = scanService;
        }

        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
        public ApiResponse<ScanResponse> createScan(
                        @Valid @RequestBody CreateScanRequest request) {

                return new ApiResponse<>(
                                true,
                                "Scan created successfully",
                                scanService.createScan(request));
        }

        @GetMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'PENTESTER')")
        public ApiResponse<PageResponse<ScanResponse>> getAllScans(
                        Pageable pageable) {

                return new ApiResponse<>(
                                true,
                                "Scans retrieved successfully",
                                scanService.getAllScans(pageable));
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'PENTESTER')")
        public ApiResponse<ScanResponse> getScanById(
                        @PathVariable Long id) {

                return new ApiResponse<>(
                                true,
                                "Scan retrieved successfully",
                                scanService.getScanById(id));
        }

        @GetMapping("/{id}/details")
        @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'PENTESTER')")
        public ApiResponse<ScanDetailsResponse> getScanDetails(
                        @PathVariable Long id) {

                return new ApiResponse<>(
                                true,
                                "Scan details retrieved successfully",
                                scanService.getScanDetails(id));
        }

        @GetMapping("/asset/{assetId}")
        @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'PENTESTER')")
        public ApiResponse<List<ScanResponse>> getScansByAsset(
                        @PathVariable Long assetId) {

                return new ApiResponse<>(
                                true,
                                "Asset scan history retrieved successfully",
                                scanService.getScansByAsset(assetId));
        }

        @PutMapping("/{id}/status")
        @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
        public ApiResponse<ScanResponse> updateScanStatus(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateScanStatusRequest request) {

                return new ApiResponse<>(
                                true,
                                "Scan status updated successfully",
                                scanService.updateScanStatus(id, request));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<Void> deleteScan(
                        @PathVariable Long id) {

                scanService.deleteScan(id);

                return new ApiResponse<>(
                                true,
                                "Scan deleted successfully",
                                null);
        }
}