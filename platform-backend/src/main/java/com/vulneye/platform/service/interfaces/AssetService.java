package com.vulneye.platform.service.interfaces;

import com.vulneye.platform.dto.asset.AssetResponse;
import com.vulneye.platform.dto.asset.CreateAssetRequest;
import com.vulneye.platform.dto.asset.UpdateAssetRequest;
import com.vulneye.platform.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AssetService {

    AssetResponse createAsset(CreateAssetRequest request);

    PageResponse<AssetResponse> getAllAssets(Pageable pageable);

    AssetResponse getAssetById(Long id);

    AssetResponse updateAsset(Long id, UpdateAssetRequest request);

    void deleteAsset(Long id);
}