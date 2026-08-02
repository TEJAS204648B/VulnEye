package com.vulneye.platform.service.impl;

import com.vulneye.platform.dto.response.FindingResponse;
import com.vulneye.platform.entity.Finding;
import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.infrastructure.parser.dto.NmapHostResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapPortResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapScanResult;
import com.vulneye.platform.repository.FindingRepository;
import com.vulneye.platform.service.interfaces.FindingService;
import com.vulneye.platform.service.interfaces.VulnerabilityService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FindingServiceImpl implements FindingService {

    private final FindingRepository findingRepository;
    private final VulnerabilityService vulnerabilityService;

    public FindingServiceImpl(FindingRepository findingRepository, VulnerabilityService vulnerabilityService) {
        this.findingRepository = findingRepository;
        this.vulnerabilityService = vulnerabilityService;
    }

    @Override
    public void saveFindings(Scan scan, NmapScanResult scanResult) {

        List<Finding> findings = new ArrayList<>();

        for (NmapHostResult host : scanResult.getHosts()) {

            for (NmapPortResult port : host.getPorts()) {

                Finding finding = new Finding();

                finding.setScan(scan);
                finding.setHostAddress(host.getAddress());
                finding.setHostname(host.getHostname());

                finding.setPort(Integer.parseInt(port.getPort()));
                finding.setProtocol(port.getProtocol());
                finding.setService(
                        port.getService() != null
                                ? port.getService()
                                : "unknown");

                finding.setProduct(port.getProduct());
                finding.setVersion(port.getVersion());
                finding.setExtraInfo(port.getExtraInfo());

                finding.setState(port.getState());

                System.out.println("--------------------------------");
                System.out.println("Host     : " + host.getAddress());
                System.out.println("Port     : " + port.getPort());
                System.out.println("Protocol : " + port.getProtocol());
                System.out.println("Service  : " + port.getService());
                System.out.println("State    : " + port.getState());
                System.out.println("Product  : " + port.getProduct());

                findings.add(finding);
            }
        }

        if (!findings.isEmpty()) {
            findingRepository.saveAll(findings);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FindingResponse> getFindingsByScanId(Long scanId) {

        List<Finding> findings = findingRepository.findByScanId(scanId);

        List<FindingResponse> responses = new ArrayList<>();

        for (Finding finding : findings) {

            FindingResponse response = new FindingResponse();

            response.setHostAddress(finding.getHostAddress());
            response.setHostname(finding.getHostname());
            response.setPort(finding.getPort());
            response.setProtocol(finding.getProtocol());
            response.setService(finding.getService());

            response.setProduct(finding.getProduct());
            response.setVersion(finding.getVersion());
            response.setExtraInfo(finding.getExtraInfo());

            response.setState(finding.getState());

            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Finding> findByScan(Scan scan) {
        return findingRepository.findByScan(scan);
    }
}