package com.vulneye.platform.infrastructure.parser.dto;

import java.util.ArrayList;
import java.util.List;

public class NmapHostResult {

    private String address;

    private String hostname;

    private String status;

    private List<NmapPortResult> ports = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NmapPortResult> getPorts() {
        return ports;
    }

    public void setPorts(List<NmapPortResult> ports) {
        this.ports = ports;
    }
}