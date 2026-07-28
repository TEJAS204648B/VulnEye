package com.vulneye.platform.infrastructure.parser;

import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.infrastructure.parser.dto.NmapScanResult;

import java.io.IOException;

public interface NmapXmlParser {

    NmapScanResult parse(Scan scan) throws IOException;

}