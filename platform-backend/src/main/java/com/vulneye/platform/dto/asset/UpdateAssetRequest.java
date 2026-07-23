package com.vulneye.platform.dto.asset;

import com.vulneye.platform.entity.enums.AssetStatus;
import com.vulneye.platform.entity.enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateAssetRequest {

    @NotBlank(message = "Asset name is required")
    @Size(max = 100, message = "Asset name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Target is required")
    @Size(max = 255, message = "Target must not exceed 255 characters")
    private String target;

    @NotNull(message = "Asset type is required")
    private AssetType type;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Asset status is required")
    private AssetStatus status;

    public UpdateAssetRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AssetStatus getStatus() {
        return status;
    }

    public void setStatus(AssetStatus status) {
        this.status = status;
    }
}