package com.vulneye.platform.infrastructure.report;

import com.vulneye.platform.entity.Scan;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ReportPathServiceImpl implements ReportPathService {

    private static final String REPORTS_DIRECTORY = "reports";
    private static final String NMAP_DIRECTORY = "nmap";

    @Override
    public String createNmapReportPath(Scan scan) throws IOException {

        Path reportDirectory = Path.of(
                REPORTS_DIRECTORY,
                NMAP_DIRECTORY);

        Files.createDirectories(reportDirectory);

        Path reportFile = reportDirectory.resolve(
                "scan-" + scan.getId() + ".xml");

        return reportFile.toString();
    }
}