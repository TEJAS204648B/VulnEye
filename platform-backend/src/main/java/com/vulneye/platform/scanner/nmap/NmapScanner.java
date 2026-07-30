package com.vulneye.platform.scanner.nmap;

import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.entity.enums.ScanType;
import com.vulneye.platform.infrastructure.parser.NmapXmlParser;
import com.vulneye.platform.infrastructure.parser.dto.NmapScanResult;
import com.vulneye.platform.infrastructure.process.CommandExecutor;
import com.vulneye.platform.infrastructure.process.CommandRequest;
import com.vulneye.platform.infrastructure.process.CommandResult;
import com.vulneye.platform.infrastructure.report.ReportPathService;
import com.vulneye.platform.scanner.AbstractScanner;
import com.vulneye.platform.service.FindingService;
import com.vulneye.platform.service.VulnerabilityService;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class NmapScanner extends AbstractScanner {

    private final CommandExecutor commandExecutor;
    private final ReportPathService reportPathService;
    private final NmapXmlParser nmapXmlParser;
    private final FindingService findingService;
    private final VulnerabilityService vulnerabilityService;

    public NmapScanner(
            CommandExecutor commandExecutor,
            ReportPathService reportPathService,
            NmapXmlParser nmapXmlParser,
            FindingService findingService,
            VulnerabilityService vulnerabilityService) {

        this.commandExecutor = commandExecutor;
        this.reportPathService = reportPathService;
        this.nmapXmlParser = nmapXmlParser;
        this.findingService = findingService;
        this.vulnerabilityService = vulnerabilityService;
    }

    @Override
    public ScanType getSupportedType() {
        return ScanType.NMAP;
    }

    @Override
    protected void performScan(Scan scan) throws Exception {

        logger.info("Executing Nmap scan against {}",
                scan.getAsset().getTarget());

        String reportPath = reportPathService.createNmapReportPath(scan);

        CommandRequest request = new CommandRequest(
                List.of(
                        "nmap",
                        "-sV",
                        "--script",
                        "vuln",
                        "-oX",
                        reportPath,
                        scan.getAsset().getTarget()));

        logger.info("Nmap command: {}", request.getCommand());
        logger.info("Report path: {}", reportPath);

        CommandResult result = commandExecutor.execute(request);

        logger.info("Nmap exit code: {}", result.getExitCode());
        logger.info("Nmap stderr: {}", result.getStderr());

        if (!result.isSuccess()) {
            throw new RuntimeException(
                    "Failed to execute Nmap: " + result.getStderr());
        }

        File reportFile = new File(reportPath);

        logger.info("Report exists: {}", reportFile.exists());
        logger.info("Report size: {} bytes", reportFile.length());

        scan.setResultPath(reportPath);

        logger.info("========================================");
        logger.info("Parsing Nmap XML report...");
        logger.info("========================================");

        NmapScanResult scanResult = nmapXmlParser.parse(scan);

        logger.info("========================================");
        logger.info("Parser returned successfully.");
        logger.info("Parsed {} host(s).", scanResult.getHosts().size());
        logger.info("========================================");

        findingService.saveFindings(scan, scanResult);

        logger.info("Findings saved successfully.");

        logger.info("Starting vulnerability scan...");

        vulnerabilityService.saveFromNmap(scan, scanResult);

        logger.info("Vulnerability scan completed.");

        logger.info("Nmap scan completed successfully.");
        logger.info("Report saved at: {}", reportPath);
    }
}