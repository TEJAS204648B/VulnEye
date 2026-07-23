package com.vulneye.platform.controller;

import com.vulneye.platform.dto.ApiResponse;
import com.vulneye.platform.dto.asset.AssetResponse;
import com.vulneye.platform.dto.asset.CreateAssetRequest;
import com.vulneye.platform.dto.asset.UpdateAssetRequest;
import com.vulneye.platform.service.interfaces.AssetService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ApiResponse<AssetResponse> createAsset(
            @Valid @RequestBody CreateAssetRequest request) {

        return new ApiResponse<>(
                true,
                "Asset created successfully",
                assetService.createAsset(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'PENTESTER')")
    public ApiResponse<List<AssetResponse>> getAllAssets() {

        return new ApiResponse<>(
                true,
                "Assets retrieved successfully",
                assetService.getAllAssets());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'PENTESTER')")
    public ApiResponse<AssetResponse> getAssetById(
            @PathVariable Long id) {

        return new ApiResponse<>(
                true,
                "Asset retrieved successfully",
                assetService.getAssetById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ApiResponse<AssetResponse> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssetRequest request) {

        return new ApiResponse<>(
                true,
                "Asset updated successfully",
                assetService.updateAsset(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteAsset(
            @PathVariable Long id) {

        assetService.deleteAsset(id);

        return new ApiResponse<>(
                true,
                "Asset deleted successfully",
                null);
    }
}