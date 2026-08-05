package com.vulneye.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulneye.platform.dto.response.FindingResponse;
import com.vulneye.platform.security.jwt.JwtAuthenticationFilter;
import com.vulneye.platform.service.interfaces.FindingService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FindingController.class)
@AutoConfigureMockMvc(addFilters = false)
class FindingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FindingService findingService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getFindingsByScanId_Success() throws Exception {

        FindingResponse response = new FindingResponse();

        response.setHostAddress("192.168.1.10");
        response.setHostname("Metasploitable");
        response.setPort(80);
        response.setProtocol("tcp");
        response.setService("http");
        response.setProduct("Apache");
        response.setVersion("2.4");
        response.setExtraInfo("Ubuntu");
        response.setState("open");

        when(findingService.getFindingsByScanId(1L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/scans/1/findings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Findings retrieved successfully"))
                .andExpect(jsonPath("$.data[0].hostAddress")
                        .value("192.168.1.10"))
                .andExpect(jsonPath("$.data[0].port").value(80))
                .andExpect(jsonPath("$.data[0].service").value("http"));

        verify(findingService).getFindingsByScanId(1L);
    }

}