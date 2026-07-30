package com.vulneye.platform.infrastructure.parser.dto;

import java.util.ArrayList;
import java.util.List;

public class NmapPortResult {

    private String port;

    private String protocol;

    private String state;

    private String service;

    private String product;

    private String version;

    private String extraInfo;

    private List<NmapScriptResult> scripts = new ArrayList<>();

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public List<NmapScriptResult> getScripts() {
        return scripts;
    }

    public void setScripts(List<NmapScriptResult> scripts) {
        this.scripts = scripts;
    }
}