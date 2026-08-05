package com.vulneye.platform.service.impl;

import com.vulneye.platform.repository.FindingRepository;
import com.vulneye.platform.service.interfaces.VulnerabilityService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vulneye.platform.dto.response.FindingResponse;
import com.vulneye.platform.entity.Finding;
import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.infrastructure.parser.dto.NmapHostResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapPortResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapScanResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindingServiceImplTest {

    @Mock
    private FindingRepository findingRepository;

    @Mock
    private VulnerabilityService vulnerabilityService;

    @InjectMocks
    private FindingServiceImpl findingService;

    @Test
    void findByScan_Success() {

        Scan scan = new Scan();

        Finding finding = new Finding();
        finding.setScan(scan);

        when(findingRepository.findByScan(scan))
                .thenReturn(List.of(finding));

        List<Finding> findings = findingService.findByScan(scan);

        assertNotNull(findings);
        assertEquals(1, findings.size());

        verify(findingRepository).findByScan(scan);
    }

    @Test
    void getFindingsByScanId_Success() {

        Finding finding = new Finding();

        finding.setHostAddress("192.168.1.10");
        finding.setHostname("metasploitable");
        finding.setPort(80);
        finding.setProtocol("tcp");
        finding.setService("http");
        finding.setProduct("Apache");
        finding.setVersion("2.4");
        finding.setExtraInfo("Ubuntu");
        finding.setState("open");

        when(findingRepository.findByScanId(1L))
                .thenReturn(List.of(finding));

        List<FindingResponse> responses =
                findingService.getFindingsByScanId(1L);

        assertEquals(1, responses.size());

        FindingResponse response = responses.get(0);

        assertEquals("192.168.1.10", response.getHostAddress());
        assertEquals(80, response.getPort());
        assertEquals("http", response.getService());

        verify(findingRepository).findByScanId(1L);
    }

    @Test
    void saveFindings_Success() {
    
        Scan scan = new Scan();

        NmapScanResult result = new NmapScanResult();

        NmapHostResult host = new NmapHostResult();

        host.setAddress("192.168.1.10");
        host.setHostname("metasploitable");

        NmapPortResult port = new NmapPortResult();

        port.setPort("80");
        port.setProtocol("tcp");
        port.setService("http");
        port.setProduct("Apache");
        port.setVersion("2.4");
        port.setExtraInfo("Ubuntu");
        port.setState("open");

        host.getPorts().add(port);

        result.getHosts().add(host);

        findingService.saveFindings(scan, result);

        verify(findingRepository).saveAll(anyList());
    }

}