package com.vulneye.platform.infrastructure.process;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class CommandExecutor {

    public CommandResult execute(CommandRequest request)
            throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder(request.getCommand());

        Process process = processBuilder.start();

        String stdout = readStream(process.getInputStream());
        String stderr = readStream(process.getErrorStream());

        int exitCode = process.waitFor();

        return new CommandResult(
                exitCode,
                stdout,
                stderr);
    }

    private String readStream(java.io.InputStream inputStream)
            throws IOException {

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line)
                        .append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}