package com.vulneye.platform.scanner;

import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.entity.enums.ScanType;

public interface Scanner {

    ScanType getSupportedType();

    void execute(Scan scan);

}