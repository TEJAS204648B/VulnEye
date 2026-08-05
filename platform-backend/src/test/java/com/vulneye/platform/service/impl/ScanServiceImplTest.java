package com.vulneye.platform.service.impl;

import com.vulneye.platform.repository.AssetRepository;
import com.vulneye.platform.repository.FindingRepository;
import com.vulneye.platform.repository.ScanRepository;
import com.vulneye.platform.repository.VulnerabilityRepository;
import com.vulneye.platform.service.interfaces.FindingService;
import com.vulneye.platform.service.interfaces.ScanExecutionService;
import com.vulneye.platform.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vulneye.platform.dto.scan.CreateScanRequest;
import com.vulneye.platform.dto.scan.ScanResponse;
import com.vulneye.platform.dto.scan.UpdateScanStatusRequest;
import com.vulneye.platform.entity.Asset;
import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.entity.enums.AssetStatus;
import com.vulneye.platform.entity.enums.AssetType;
import com.vulneye.platform.entity.enums.ScanStatus;
import com.vulneye.platform.entity.enums.ScanType;

import com.vulneye.platform.dto.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScanServiceImplTest {

    @Mock
    private ScanRepository scanRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private ScanExecutionService scanExecutionService;

    @Mock
    private FindingRepository findingRepository;

    @Mock
    private VulnerabilityRepository vulnerabilityRepository;

    @Mock
    private FindingService findingService;

    @InjectMocks
    private ScanServiceImpl scanService;

    @Test
    void createScan_Success() {

        Asset asset = new Asset();

        try {

            Field assetId = Asset.class.getDeclaredField("id");
            assetId.setAccessible(true);
            assetId.set(asset, 1L);

        } catch (Exception e) {

            fail("Unable to set Asset ID", e);
        }

        asset.setName("Google");
        asset.setTarget("google.com");
        asset.setType(AssetType.WEB);
        asset.setStatus(AssetStatus.ACTIVE);

        CreateScanRequest request = new CreateScanRequest();

        request.setAssetId(1L);
        request.setScanType(ScanType.NMAP);

        when(assetRepository.findById(1L))
                .thenReturn(java.util.Optional.of(asset));

        Scan savedScan = new Scan();

        try {

            Field scanId = Scan.class.getDeclaredField("id");
            scanId.setAccessible(true);
            scanId.set(savedScan, 100L);

        } catch (Exception e) {

            fail("Unable to set Scan ID", e);
        }

        savedScan.setAsset(asset);
        savedScan.setScanType(ScanType.NMAP);
        savedScan.setStatus(ScanStatus.PENDING);

        when(scanRepository.save(any(Scan.class)))
                .thenReturn(savedScan);

        doNothing().when(scanExecutionService)
                .executeScan(any(Scan.class));

        ScanResponse response = scanService.createScan(request);

        assertNotNull(response);

        assertEquals(100L, response.getId());
        assertEquals(1L, response.getAssetId());
        assertEquals("Google", response.getAssetName());
        assertEquals(ScanType.NMAP, response.getScanType());
        assertEquals(ScanStatus.PENDING, response.getStatus());

        verify(assetRepository).findById(1L);
        verify(scanRepository).save(any(Scan.class));
        verify(scanExecutionService).executeScan(any(Scan.class));
        verifyNoMoreInteractions(
                assetRepository,
                scanRepository,
                scanExecutionService);
    }

    @Test
    void createScan_AssetNotFound() {

        CreateScanRequest request = new CreateScanRequest();

        request.setAssetId(99L);
        request.setScanType(ScanType.NMAP);

        when(assetRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> scanService.createScan(request));

        assertEquals(
                "Asset not found with id: 99",
                exception.getMessage());

        verify(assetRepository).findById(99L);
        verify(scanRepository, never()).save(any());
        verify(scanExecutionService, never()).executeScan(any());
    }

    @Test
    void getScanById_Success() {

        Asset asset = new Asset();

        try {

            Field assetId = Asset.class.getDeclaredField("id");
            assetId.setAccessible(true);
            assetId.set(asset, 1L);

        } catch (Exception e) {

            fail(e);
        }

        asset.setName("Google");

        Scan scan = new Scan();

        try {

            Field scanId = Scan.class.getDeclaredField("id");
            scanId.setAccessible(true);
            scanId.set(scan, 10L);

        } catch (Exception e) {

            fail(e);
        }

        scan.setAsset(asset);
        scan.setScanType(ScanType.NMAP);
        scan.setStatus(ScanStatus.COMPLETED);

        when(scanRepository.findById(10L))
                .thenReturn(java.util.Optional.of(scan));

        ScanResponse response = scanService.getScanById(10L);

        assertNotNull(response);

        assertEquals(10L, response.getId());
        assertEquals(1L, response.getAssetId());
        assertEquals("Google", response.getAssetName());

        verify(scanRepository).findById(10L);
    }

    @Test
    void getScanById_NotFound() {

        when(scanRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> scanService.getScanById(99L));

        assertEquals(
                "Scan not found with id: 99",
                exception.getMessage());

        verify(scanRepository).findById(99L);
    }

    @Test
    void getAllScans_Page() {

        Asset asset = new Asset();

        try {

            Field assetId = Asset.class.getDeclaredField("id");
            assetId.setAccessible(true);
            assetId.set(asset, 1L);

        } catch (Exception e) {

            fail(e);
        }

        asset.setName("Google");

        Scan scan = new Scan();

        try {

            Field scanId = Scan.class.getDeclaredField("id");
            scanId.setAccessible(true);
            scanId.set(scan, 1L);

        } catch (Exception e) {

            fail(e);
        }

        scan.setAsset(asset);
        scan.setScanType(ScanType.NMAP);
        scan.setStatus(ScanStatus.COMPLETED);

        Page<Scan> page = new PageImpl<>(
                Collections.singletonList(scan),
                PageRequest.of(0, 20),
                1);

        Pageable pageable = PageRequest.of(0, 20);

        when(scanRepository.findAll(pageable))
                .thenReturn(page);

        PageResponse<ScanResponse> response = scanService.getAllScans(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());

        verify(scanRepository).findAll(pageable);
    }

    @Test
    void getScansByAsset_Success() {

        Asset asset = new Asset();

        try {

            Field assetId = Asset.class.getDeclaredField("id");
            assetId.setAccessible(true);
            assetId.set(asset, 1L);

        } catch (Exception e) {

            fail(e);
        }

        asset.setName("Google");

        Scan scan = new Scan();

        try {

            Field scanId = Scan.class.getDeclaredField("id");
            scanId.setAccessible(true);
            scanId.set(scan, 100L);

        } catch (Exception e) {

            fail(e);
        }

        scan.setAsset(asset);
        scan.setScanType(ScanType.NMAP);
        scan.setStatus(ScanStatus.COMPLETED);

        when(assetRepository.findById(1L))
                .thenReturn(java.util.Optional.of(asset));

        when(scanRepository.findByAsset(asset))
                .thenReturn(List.of(scan));

        List<ScanResponse> responses = scanService.getScansByAsset(1L);

        assertEquals(1, responses.size());
        assertEquals(100L, responses.get(0).getId());

        verify(assetRepository).findById(1L);
        verify(scanRepository).findByAsset(asset);
    }

    @Test
    void updateScanStatus_Success() {

        Asset asset = new Asset();

        try {
            Field assetId = Asset.class.getDeclaredField("id");
            assetId.setAccessible(true);
            assetId.set(asset, 1L);
        } catch (Exception e) {
            fail(e);
        }

        asset.setName("Google");

        Scan scan = new Scan();

        scan.setAsset(asset);
        scan.setScanType(ScanType.NMAP);
        scan.setStatus(ScanStatus.RUNNING);

        UpdateScanStatusRequest request = new UpdateScanStatusRequest();

        request.setStatus(ScanStatus.COMPLETED);

        when(scanRepository.findById(1L))
                .thenReturn(java.util.Optional.of(scan));

        when(scanRepository.save(any(Scan.class)))
                .thenReturn(scan);

        ScanResponse response = scanService.updateScanStatus(1L, request);

        assertEquals(
                ScanStatus.COMPLETED,
                response.getStatus());

        assertNotNull(scan.getCompletedAt());

        verify(scanRepository).findById(1L);
        verify(scanRepository).save(scan);
    }

    @Test
    void deleteScan_Success() {

        Scan scan = new Scan();

        when(scanRepository.findById(1L))
                .thenReturn(java.util.Optional.of(scan));

        scanService.deleteScan(1L);

        verify(scanRepository).findById(1L);
        verify(scanRepository).delete(scan);
    }

    @Test
    void deleteScan_NotFound() {

        when(scanRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> scanService.deleteScan(99L));

        verify(scanRepository).findById(99L);
        verify(scanRepository, never()).delete(any());
    }

}