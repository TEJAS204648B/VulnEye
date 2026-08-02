package com.vulneye.platform.service.impl;

import com.vulneye.platform.dto.asset.AssetResponse;
import com.vulneye.platform.dto.asset.CreateAssetRequest;
import com.vulneye.platform.dto.asset.UpdateAssetRequest;
import com.vulneye.platform.dto.common.PageResponse;
import com.vulneye.platform.entity.Asset;
import com.vulneye.platform.exception.BadRequestException;
import com.vulneye.platform.exception.ResourceNotFoundException;
import com.vulneye.platform.repository.AssetRepository;
import com.vulneye.platform.service.interfaces.AssetService;
import com.vulneye.platform.util.PageMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public AssetResponse createAsset(CreateAssetRequest request) {

        String normalizedTarget = normalizeTarget(request.getTarget());

        if (assetRepository.existsByTarget(normalizedTarget)) {
            throw new BadRequestException("Asset with this target already exists");
        }

        Asset asset = new Asset();
        asset.setName(request.getName());
        asset.setTarget(normalizedTarget);
        asset.setType(request.getType());
        asset.setDescription(request.getDescription());
        asset.setStatus(request.getStatus());

        Asset savedAsset = assetRepository.save(asset);

        return mapToResponse(savedAsset);
    }

    @Override
    public PageResponse<AssetResponse> getAllAssets(Pageable pageable) {

        Page<Asset> page = assetRepository.findAll(pageable);

        return PageMapper.map(page, this::mapToResponse);
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

        String normalizedTarget = normalizeTarget(request.getTarget());

        if (!asset.getTarget().equals(normalizedTarget)
                && assetRepository.existsByTarget(normalizedTarget)) {

            throw new BadRequestException("Asset with this target already exists");
        }

        asset.setName(request.getName());
        asset.setTarget(normalizedTarget);
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

    private String normalizeTarget(String target) {

        if (target == null || target.isBlank()) {
            throw new BadRequestException("Target must not be empty");
        }

        target = target.trim();

        try {

            if (target.startsWith("http://") || target.startsWith("https://")) {

                URI uri = URI.create(target);

                if (uri.getHost() == null || uri.getHost().isBlank()) {
                    throw new BadRequestException("Invalid target");
                }

                return uri.getHost();
            }

            return target.replaceAll("/+$", "");

        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid target");
        }
    }
}