package com.vulneye.platform.service.impl;

import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.entity.enums.ScanStatus;
import com.vulneye.platform.repository.ScanRepository;
import com.vulneye.platform.scanner.factory.ScannerFactory;
import com.vulneye.platform.service.interfaces.ScanExecutionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class ScanExecutionServiceImpl implements ScanExecutionService {

    private final ScannerFactory scannerFactory;
    private final ScanRepository scanRepository;

    public ScanExecutionServiceImpl(
            ScannerFactory scannerFactory,
            ScanRepository scanRepository) {

        this.scannerFactory = scannerFactory;
        this.scanRepository = scanRepository;
    }

    @Override
    @Async
    public void executeScan(Scan scan) {

        try {

            scan.setStatus(ScanStatus.RUNNING);
            scanRepository.save(scan);

            scannerFactory
                    .getScanner(scan.getScanType())
                    .execute(scan);

            scan.setStatus(ScanStatus.COMPLETED);
            scan.setCompletedAt(LocalDateTime.now(ZoneOffset.UTC));

        } catch (Exception ex) {

            scan.setStatus(ScanStatus.FAILED);
            scan.setCompletedAt(LocalDateTime.now(ZoneOffset.UTC));

        } finally {

            scanRepository.save(scan);
        }
    }
}