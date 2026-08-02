package com.vulneye.platform.service.impl;

import com.vulneye.platform.dto.asset.AssetResponse;
import com.vulneye.platform.dto.asset.CreateAssetRequest;
import com.vulneye.platform.dto.asset.UpdateAssetRequest;
import com.vulneye.platform.entity.Asset;
import com.vulneye.platform.entity.enums.AssetStatus;
import com.vulneye.platform.entity.enums.AssetType;
import com.vulneye.platform.repository.AssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.vulneye.platform.exception.BadRequestException;
import com.vulneye.platform.exception.ResourceNotFoundException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    @Test
    void createAsset_Success() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setName("Google");
        request.setTarget("https://google.com");
        request.setType(AssetType.WEB);
        request.setDescription("Google Website");
        request.setStatus(AssetStatus.ACTIVE);

        when(assetRepository.existsByTarget("google.com"))
                .thenReturn(false);

        Asset savedAsset = new Asset();

        try {
            Field idField = Asset.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedAsset, 1L);
        } catch (Exception e) {
            fail("Failed to set Asset ID for test", e);
        }

        savedAsset.setName("Google");
        savedAsset.setTarget("google.com");
        savedAsset.setType(AssetType.WEB);
        savedAsset.setDescription("Google Website");
        savedAsset.setStatus(AssetStatus.ACTIVE);

        when(assetRepository.save(any(Asset.class)))
                .thenReturn(savedAsset);

        AssetResponse response = assetService.createAsset(request);

        assertNotNull(response);

        assertEquals(1L, response.getId());
        assertEquals("Google", response.getName());
        assertEquals("google.com", response.getTarget());
        assertEquals(AssetType.WEB, response.getType());
        assertEquals(AssetStatus.ACTIVE, response.getStatus());

        verify(assetRepository).existsByTarget("google.com");
        verify(assetRepository).save(any(Asset.class));
        verifyNoMoreInteractions(assetRepository);
    }

    @Test
    void createAsset_DuplicateTarget() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setName("Google");
        request.setTarget("https://google.com");
        request.setType(AssetType.WEB);
        request.setDescription("Google Website");
        request.setStatus(AssetStatus.ACTIVE);

        when(assetRepository.existsByTarget("google.com"))
                .thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> assetService.createAsset(request));

        assertEquals(
                "Asset with this target already exists",
                exception.getMessage());

        verify(assetRepository).existsByTarget("google.com");
        verify(assetRepository, never()).save(any());
    }

    @Test
    void getAssetById_Success() {

        Asset asset = new Asset();

        try {
            Field idField = Asset.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(asset, 1L);
        } catch (Exception e) {
            fail(e);
        }

        asset.setName("Google");
        asset.setTarget("google.com");
        asset.setType(AssetType.WEB);
        asset.setStatus(AssetStatus.ACTIVE);

        when(assetRepository.findById(1L))
                .thenReturn(java.util.Optional.of(asset));

        AssetResponse response = assetService.getAssetById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Google", response.getName());

        verify(assetRepository).findById(1L);
    }

    @Test
    void getAssetById_NotFound() {

        when(assetRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> assetService.getAssetById(99L));

        assertEquals(
                "Asset not found with id: 99",
                exception.getMessage());

        verify(assetRepository).findById(99L);
    }

    @Test
    void updateAsset_Success() {

        Asset asset = new Asset();

        try {
            Field idField = Asset.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(asset, 1L);
        } catch (Exception e) {
            fail(e);
        }

        asset.setName("Old");
        asset.setTarget("old.com");
        asset.setType(AssetType.DOMAIN);
        asset.setStatus(AssetStatus.ACTIVE);

        UpdateAssetRequest request = new UpdateAssetRequest();

        request.setName("New");
        request.setTarget("new.com");
        request.setType(AssetType.WEB);
        request.setDescription("Updated");
        request.setStatus(AssetStatus.ACTIVE);

        when(assetRepository.findById(1L))
                .thenReturn(java.util.Optional.of(asset));

        when(assetRepository.existsByTarget("new.com"))
                .thenReturn(false);

        when(assetRepository.save(any()))
                .thenReturn(asset);

        AssetResponse response = assetService.updateAsset(1L, request);

        assertEquals("New", response.getName());

        verify(assetRepository).findById(1L);
        verify(assetRepository).existsByTarget("new.com");
        verify(assetRepository).save(any());
    }

    @Test
    void deleteAsset_Success() {

        Asset asset = new Asset();

        when(assetRepository.findById(1L))
                .thenReturn(java.util.Optional.of(asset));

        assetService.deleteAsset(1L);

        verify(assetRepository).findById(1L);
        verify(assetRepository).delete(asset);
    }

    @Test
    void deleteAsset_NotFound() {

        when(assetRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> assetService.deleteAsset(99L));

        verify(assetRepository).findById(99L);
        verify(assetRepository, never()).delete(any());
    }

    
}