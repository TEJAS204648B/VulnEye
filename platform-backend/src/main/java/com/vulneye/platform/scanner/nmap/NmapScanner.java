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
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NmapScanner extends AbstractScanner {

    private final CommandExecutor commandExecutor;
    private final ReportPathService reportPathService;
    private final NmapXmlParser nmapXmlParser;
    private final FindingService findingService;

    public NmapScanner(
            CommandExecutor commandExecutor,
            ReportPathService reportPathService,
            NmapXmlParser nmapXmlParser,
            FindingService findingService) {

        this.commandExecutor = commandExecutor;
        this.reportPathService = reportPathService;
        this.nmapXmlParser = nmapXmlParser;
        this.findingService = findingService;
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
                        "-oX",
                        reportPath,
                        scan.getAsset().getTarget()));

        CommandResult result = commandExecutor.execute(request);

        if (!result.isSuccess()) {
            throw new RuntimeException(
                    "Failed to execute Nmap: " + result.getStderr());
        }

        scan.setResultPath(reportPath);

        logger.info("Parsing Nmap XML report...");

        NmapScanResult scanResult = nmapXmlParser.parse(scan);

        logger.info("Parsed {} host(s).", scanResult.getHosts().size());

        findingService.saveFindings(scan, scanResult);

        logger.info("Findings saved successfully.");

        logger.info("Nmap scan completed successfully.");
        logger.info("Report saved at: {}", reportPath);
    }
}