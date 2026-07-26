package com.vulneye.platform.scanner.nmap;

import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.entity.enums.ScanType;
import com.vulneye.platform.infrastructure.process.CommandExecutor;
import com.vulneye.platform.infrastructure.process.CommandResult;
import com.vulneye.platform.scanner.AbstractScanner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NmapScanner extends AbstractScanner {

    private final CommandExecutor commandExecutor;

    public NmapScanner(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public ScanType getSupportedType() {
        return ScanType.NMAP;
    }

    @Override
    protected void performScan(Scan scan) throws Exception {

        logger.info("Executing Nmap scan against {}",
                scan.getAsset().getTarget());

        CommandResult result = commandExecutor.execute(
                List.of("nmap", "--version"));

        if (!result.isSuccess()) {
            throw new RuntimeException(
                    "Failed to execute Nmap: " + result.getStderr());
        }

        logger.info("Nmap version information:\n{}",
                result.getStdout());
    }
}