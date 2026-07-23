package com.vulneye.platform.service.impl;

import com.vulneye.platform.dto.asset.AssetResponse;
import com.vulneye.platform.dto.asset.CreateAssetRequest;
import com.vulneye.platform.dto.asset.UpdateAssetRequest;
import com.vulneye.platform.entity.Asset;
import com.vulneye.platform.exception.BadRequestException;
import com.vulneye.platform.exception.ResourceNotFoundException;
import com.vulneye.platform.repository.AssetRepository;
import com.vulneye.platform.service.interfaces.AssetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public AssetResponse createAsset(CreateAssetRequest request) {

        if (assetRepository.existsByTarget(request.getTarget())) {
            throw new BadRequestException("Asset with this target already exists");
        }

        Asset asset = new Asset();
        asset.setName(request.getName());
        asset.setTarget(request.getTarget());
        asset.setType(request.getType());
        asset.setDescription(request.getDescription());
        asset.setStatus(request.getStatus());

        Asset savedAsset = assetRepository.save(asset);

        return mapToResponse(savedAsset);
    }

    @Override
    public List<AssetResponse> getAllAssets() {

        return assetRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AssetResponse getAssetById(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        return mapToResponse(asset);
    }

    @Override
    public AssetResponse updateAsset(Long id, UpdateAssetRequest request) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        if (!asset.getTarget().equals(request.getTarget())
                && assetRepository.existsByTarget(request.getTarget())) {

            throw new BadRequestException("Asset with this target already exists");
        }

        asset.setName(request.getName());
        asset.setTarget(request.getTarget());
        asset.setType(request.getType());
        asset.setDescription(request.getDescription());
        asset.setStatus(request.getStatus());

        Asset updatedAsset = assetRepository.save(asset);

        return mapToResponse(updatedAsset);
    }

    @Override
    public void deleteAsset(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        assetRepository.delete(asset);
    }

    private AssetResponse mapToResponse(Asset asset) {

        AssetResponse response = new AssetResponse();

        response.setId(asset.getId());
        response.setName(asset.getName());
        response.setTarget(asset.getTarget());
        response.setType(asset.getType());
        response.setDescription(asset.getDescription());
        response.setStatus(asset.getStatus());
        response.setCreatedAt(asset.getCreatedAt());
        response.setUpdatedAt(asset.getUpdatedAt());

        return response;
    }
}