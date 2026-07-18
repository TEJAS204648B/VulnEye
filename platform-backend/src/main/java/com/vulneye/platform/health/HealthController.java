package com.vulneye.platform.health;

import com.vulneye.platform.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ApiResponse<Map<String, String>> health() {

        return new ApiResponse<>(
                true,
                "Health check successful",
                Map.of("status", "UP"));
    }
}