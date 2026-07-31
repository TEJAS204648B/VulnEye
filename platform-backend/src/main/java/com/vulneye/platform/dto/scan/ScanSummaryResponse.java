package com.vulneye.platform.dto.scan;

public class ScanSummaryResponse {

    private int findings;

    private int vulnerabilities;

    private int critical;

    private int high;

    private int medium;

    private int low;

    public int getFindings() {
        return findings;
    }

    public void setFindings(int findings) {
        this.findings = findings;
    }

    public int getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(int vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }
}