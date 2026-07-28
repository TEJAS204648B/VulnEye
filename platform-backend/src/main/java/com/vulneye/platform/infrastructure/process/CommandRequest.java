package com.vulneye.platform.infrastructure.process;

import java.util.List;
import java.util.Objects;

public class CommandRequest {

    private final List<String> command;

    public CommandRequest(List<String> command) {
        this.command = List.copyOf(
                Objects.requireNonNull(command,
                        "Command must not be null"));
    }

    public List<String> getCommand() {
        return command;
    }
}