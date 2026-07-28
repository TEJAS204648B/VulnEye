package com.vulneye.platform.infrastructure.parser.dto;

import java.util.ArrayList;
import java.util.List;

public class NmapScanResult {

    private List<NmapHostResult> hosts = new ArrayList<>();

    public List<NmapHostResult> getHosts() {
        return hosts;
    }

    public void setHosts(List<NmapHostResult> hosts) {
        this.hosts = hosts;
    }
}