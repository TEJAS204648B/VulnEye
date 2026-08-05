package com.vulneye.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulneye.platform.dto.common.PageResponse;
import com.vulneye.platform.dto.scan.CreateScanRequest;
import com.vulneye.platform.dto.scan.ScanDetailsResponse;
import com.vulneye.platform.dto.scan.ScanResponse;
import com.vulneye.platform.entity.enums.ScanStatus;
import com.vulneye.platform.entity.enums.ScanType;
import com.vulneye.platform.security.jwt.JwtAuthenticationFilter;
import com.vulneye.platform.service.interfaces.ScanService;

import com.vulneye.platform.dto.scan.UpdateScanStatusRequest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScanController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScanService scanService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ScanResponse buildResponse() {

        ScanResponse response = new ScanResponse();

        response.setId(1L);
        response.setAssetId(10L);
        response.setAssetName("Metasploitable");
        response.setScanType(ScanType.NMAP);
        response.setStatus(ScanStatus.COMPLETED);
        response.setStartedAt(LocalDateTime.now());
        response.setCompletedAt(LocalDateTime.now());
        response.setResultPath("reports/nmap.xml");
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createScan_Success() throws Exception {

        CreateScanRequest request = new CreateScanRequest();

        request.setAssetId(10L);
        request.setScanType(ScanType.NMAP);

        when(scanService.createScan(any(CreateScanRequest.class)))
                .thenReturn(buildResponse());

        mockMvc.perform(post("/api/scans")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Scan created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.assetId").value(10))
                .andExpect(jsonPath("$.data.assetName").value("Metasploitable"));

        verify(scanService).createScan(any(CreateScanRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createScan_InvalidRequest() throws Exception {

        CreateScanRequest request = new CreateScanRequest();

        mockMvc.perform(post("/api/scans")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(scanService, never()).createScan(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllScans_Success() throws Exception {

        PageResponse<ScanResponse> page = new PageResponse<>();

        page.setContent(List.of(buildResponse()));
        page.setPage(0);
        page.setSize(20);
        page.setTotalElements(1);
        page.setTotalPages(1);
        page.setFirst(true);
        page.setLast(true);

        when(scanService.getAllScans(any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/scans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Scans retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].assetName").value("Metasploitable"));

        verify(scanService).getAllScans(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getScanById_Success() throws Exception {

        when(scanService.getScanById(1L))
                .thenReturn(buildResponse());

        mockMvc.perform(get("/api/scans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Scan retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.assetId").value(10))
                .andExpect(jsonPath("$.data.assetName").value("Metasploitable"));

        verify(scanService).getScanById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getScanDetails_Success() throws Exception {

        ScanDetailsResponse details = new ScanDetailsResponse();

        details.setScan(buildResponse());

        when(scanService.getScanDetails(1L))
                .thenReturn(details);

        mockMvc.perform(get("/api/scans/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Scan details retrieved successfully"))
                .andExpect(jsonPath("$.data.scan.id").value(1))
                .andExpect(jsonPath("$.data.scan.assetId").value(10));

        verify(scanService).getScanDetails(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getScansByAsset_Success() throws Exception {

        when(scanService.getScansByAsset(10L))
                .thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/api/scans/asset/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Asset scan history retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].assetId").value(10));

        verify(scanService).getScansByAsset(10L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateScanStatus_Success() throws Exception {

        UpdateScanStatusRequest request = new UpdateScanStatusRequest();
        request.setStatus(ScanStatus.COMPLETED);

        ScanResponse response = buildResponse();
        response.setStatus(ScanStatus.COMPLETED);

        when(scanService.updateScanStatus(
                eq(1L),
                any(UpdateScanStatusRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/scans/1/status")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Scan status updated successfully"))
                .andExpect(jsonPath("$.data.status")
                        .value("COMPLETED"));

        verify(scanService)
                .updateScanStatus(eq(1L), any(UpdateScanStatusRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteScan_Success() throws Exception {

        doNothing().when(scanService).deleteScan(1L);

        mockMvc.perform(delete("/api/scans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Scan deleted successfully"));

        verify(scanService).deleteScan(1L);
    }

}