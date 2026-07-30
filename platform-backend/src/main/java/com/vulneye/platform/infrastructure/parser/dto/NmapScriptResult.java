package com.vulneye.platform.infrastructure.parser.dto;

public class NmapScriptResult {

    private String id;

    private String output;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}