package com.vulneye.platform.service.interfaces;

import com.vulneye.platform.dto.asset.AssetResponse;
import com.vulneye.platform.dto.asset.CreateAssetRequest;
import com.vulneye.platform.dto.asset.UpdateAssetRequest;

import java.util.List;

public interface AssetService {

    AssetResponse createAsset(CreateAssetRequest request);

    List<AssetResponse> getAllAssets();

    AssetResponse getAssetById(Long id);

    AssetResponse updateAsset(Long id, UpdateAssetRequest request);

    void deleteAsset(Long id);
}