package com.vulneye.platform.service.impl;

import com.vulneye.platform.dto.scan.CreateScanRequest;
import com.vulneye.platform.dto.scan.ScanResponse;
import com.vulneye.platform.dto.scan.UpdateScanStatusRequest;
import com.vulneye.platform.entity.Asset;
import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.entity.enums.ScanStatus;
import com.vulneye.platform.exception.ResourceNotFoundException;
import com.vulneye.platform.repository.AssetRepository;
import com.vulneye.platform.repository.ScanRepository;
import com.vulneye.platform.service.interfaces.ScanExecutionService;
import com.vulneye.platform.service.interfaces.ScanService;
import org.springframework.stereotype.Service;
import com.vulneye.platform.scanner.factory.ScannerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScanServiceImpl implements ScanService {

    private final ScanRepository scanRepository;
    private final AssetRepository assetRepository;
    private final ScanExecutionService scanExecutionService;

    public ScanServiceImpl(
            ScanRepository scanRepository,
            AssetRepository assetRepository,
            ScanExecutionService scanExecutionService) {

        this.scanRepository = scanRepository;
        this.assetRepository = assetRepository;
        this.scanExecutionService = scanExecutionService;;
    }

    @Override
    public ScanResponse createScan(CreateScanRequest request) {

        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset not found with id: " + request.getAssetId()));

        Scan scan = new Scan();

        scan.setAsset(asset);
        scan.setScanType(request.getScanType());
        scan.setStatus(ScanStatus.PENDING);
        scan.setStartedAt(LocalDateTime.now());

        Scan savedScan = scanRepository.save(scan);

        scanExecutionService.executeScan(savedScan);

        return mapToResponse(savedScan);
    }

    @Override
    public List<ScanResponse> getAllScans() {

        return scanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ScanResponse getScanById(Long id) {

        Scan scan = scanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Scan not found with id: " + id));

        return mapToResponse(scan);
    }

    @Override
    public List<ScanResponse> getScansByAsset(Long assetId) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset not found with id: " + assetId));

        return scanRepository.findByAsset(asset)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ScanResponse updateScanStatus(Long id,
            UpdateScanStatusRequest request) {

        Scan scan = scanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Scan not found with id: " + id));

        scan.setStatus(request.getStatus());

        if (request.getStatus() == ScanStatus.COMPLETED
                || request.getStatus() == ScanStatus.FAILED
                || request.getStatus() == ScanStatus.CANCELLED) {

            scan.setCompletedAt(LocalDateTime.now());
        }

        Scan updatedScan = scanRepository.save(scan);

        return mapToResponse(updatedScan);
    }

    @Override
    public void deleteScan(Long id) {

        Scan scan = scanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Scan not found with id: " + id));

        scanRepository.delete(scan);
    }

    private ScanResponse mapToResponse(Scan scan) {

        ScanResponse response = new ScanResponse();

        response.setId(scan.getId());
        response.setAssetId(scan.getAsset().getId());
        response.setAssetName(scan.getAsset().getName());
        response.setScanType(scan.getScanType());
        response.setStatus(scan.getStatus());
        response.setStartedAt(scan.getStartedAt());
        response.setCompletedAt(scan.getCompletedAt());
        response.setResultPath(scan.getResultPath());
        response.setCreatedAt(scan.getCreatedAt());
        response.setUpdatedAt(scan.getUpdatedAt());

        return response;
    }
}