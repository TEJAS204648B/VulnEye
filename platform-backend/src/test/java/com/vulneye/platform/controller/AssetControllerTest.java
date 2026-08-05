package com.vulneye.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulneye.platform.dto.asset.AssetResponse;
import com.vulneye.platform.dto.asset.CreateAssetRequest;
import com.vulneye.platform.dto.asset.UpdateAssetRequest;
import com.vulneye.platform.dto.common.PageResponse;
import com.vulneye.platform.entity.enums.AssetStatus;
import com.vulneye.platform.entity.enums.AssetType;
import com.vulneye.platform.security.jwt.JwtAuthenticationFilter;
import com.vulneye.platform.service.interfaces.AssetService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssetService assetService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private AssetResponse buildResponse() {

        AssetResponse response = new AssetResponse();

        response.setId(1L);
        response.setName("Google");
        response.setTarget("google.com");
        response.setType(AssetType.WEB);
        response.setDescription("Google Website");
        response.setStatus(AssetStatus.ACTIVE);
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAsset_Success() throws Exception {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setName("Google");
        request.setTarget("https://google.com");
        request.setType(AssetType.WEB);
        request.setDescription("Google Website");
        request.setStatus(AssetStatus.ACTIVE);

        when(assetService.createAsset(any(CreateAssetRequest.class)))
                .thenReturn(buildResponse());

        mockMvc.perform(post("/api/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Asset created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Google"))
                .andExpect(jsonPath("$.data.target").value("google.com"))
                .andExpect(jsonPath("$.data.type").value("WEB"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        verify(assetService).createAsset(any(CreateAssetRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAssets_Success() throws Exception {

        PageResponse<AssetResponse> page = new PageResponse<>();

        page.setContent(List.of(buildResponse()));
        page.setPage(0);
        page.setSize(20);
        page.setTotalElements(1);
        page.setTotalPages(1);
        page.setFirst(true);
        page.setLast(true);

        when(assetService.getAllAssets(any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Assets retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].name").value("Google"))
                .andExpect(jsonPath("$.data.content[0].target").value("google.com"))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(assetService).getAllAssets(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAssetById_Success() throws Exception {

        when(assetService.getAssetById(1L))
                .thenReturn(buildResponse());

        mockMvc.perform(get("/api/assets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Asset retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Google"))
                .andExpect(jsonPath("$.data.target").value("google.com"))
                .andExpect(jsonPath("$.data.type").value("WEB"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        verify(assetService).getAssetById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAsset_Success() throws Exception {

        UpdateAssetRequest request = new UpdateAssetRequest();

        request.setName("Google Updated");
        request.setTarget("https://google.com");
        request.setType(AssetType.WEB);
        request.setDescription("Updated Description");
        request.setStatus(AssetStatus.ACTIVE);

        AssetResponse response = buildResponse();
        response.setName("Google Updated");
        response.setDescription("Updated Description");

        when(assetService.updateAsset(eq(1L), any(UpdateAssetRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/assets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Asset updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Google Updated"))
                .andExpect(jsonPath("$.data.target").value("google.com"));

        verify(assetService).updateAsset(eq(1L), any(UpdateAssetRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAsset_Success() throws Exception {

        doNothing().when(assetService).deleteAsset(1L);

        mockMvc.perform(delete("/api/assets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Asset deleted successfully"));

        verify(assetService).deleteAsset(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAsset_InvalidRequest() throws Exception {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setName("");
        request.setTarget("");
        request.setDescription("Invalid Asset");

        mockMvc.perform(post("/api/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(assetService, never()).createAsset(any());
    }

}