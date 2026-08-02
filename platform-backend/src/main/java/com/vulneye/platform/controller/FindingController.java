package com.vulneye.platform.controller;

import com.vulneye.platform.dto.ApiResponse;
import com.vulneye.platform.dto.response.FindingResponse;
import com.vulneye.platform.service.interfaces.FindingService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class FindingController {

    private final FindingService findingService;

    public FindingController(FindingService findingService) {
        this.findingService = findingService;
    }

    @GetMapping("/{scanId}/findings")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'PENTESTER')")
    public ApiResponse<List<FindingResponse>> getFindingsByScanId(
            @PathVariable Long scanId) {

        return new ApiResponse<>(
                true,
                "Findings retrieved successfully",
                findingService.getFindingsByScanId(scanId));
    }
}