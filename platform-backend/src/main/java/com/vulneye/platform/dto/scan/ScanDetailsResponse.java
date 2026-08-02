package com.vulneye.platform.dto.scan;

import com.vulneye.platform.dto.response.FindingResponse;

import java.util.List;

public class ScanDetailsResponse {

    private ScanResponse scan;

    private ScanSummaryResponse summary;
    private List<FindingResponse> findings;

    public ScanResponse getScan() {
        return scan;
    }

    public void setScan(ScanResponse scan) {
        this.scan = scan;
    }

    public ScanSummaryResponse getSummary() {
        return summary;
    }

    public void setSummary(ScanSummaryResponse summary) {
        this.summary = summary;
    }

    public List<FindingResponse> getFindings() {
        return findings;
    }

    public void setFindings(List<FindingResponse> findings) {
        this.findings = findings;
    }

}