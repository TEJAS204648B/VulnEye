package com.vulneye.platform.infrastructure.report;

import com.vulneye.platform.entity.Scan;

import java.io.IOException;

public interface ReportPathService {

    String createNmapReportPath(Scan scan) throws IOException;

}