package com.vulneye.platform.scanner.factory;

import com.vulneye.platform.entity.enums.ScanType;
import com.vulneye.platform.scanner.Scanner;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ScannerFactory {

    private final Map<ScanType, Scanner> scannerMap = new EnumMap<>(ScanType.class);

    public ScannerFactory(List<Scanner> scanners) {

        for (Scanner scanner : scanners) {
            scannerMap.put(scanner.getSupportedType(), scanner);
        }
    }

    public Scanner getScanner(ScanType scanType) {

        Scanner scanner = scannerMap.get(scanType);

        if (scanner == null) {
            throw new IllegalArgumentException(
                    "No scanner registered for type: " + scanType
            );
        }

        return scanner;
    }
}