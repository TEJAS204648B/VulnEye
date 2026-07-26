package com.vulneye.platform.scanner;

import com.vulneye.platform.entity.Scan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractScanner implements Scanner {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final void execute(Scan scan) {

        logger.info("Starting {} scan for asset: {}",
                getSupportedType(),
                scan.getAsset().getTarget());

        try {

            performScan(scan);

            logger.info("{} scan completed successfully.",
                    getSupportedType());

        } catch (Exception ex) {

            logger.error("{} scan failed: {}",
                    getSupportedType(),
                    ex.getMessage(),
                    ex);

            throw new RuntimeException(ex);
        }
    }

    /**
     * Scanner-specific implementation.
     */
    protected abstract void performScan(Scan scan) throws Exception;

}