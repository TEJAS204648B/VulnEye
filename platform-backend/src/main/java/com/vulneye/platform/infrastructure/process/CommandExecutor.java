package com.vulneye.platform.infrastructure.process;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Component
public class CommandExecutor {

    public CommandResult execute(CommandRequest request)
            throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder(request.getCommand());

        Process process = processBuilder.start();

        System.out.println("Process started...");

        CompletableFuture<String> stdoutFuture = CompletableFuture
                .supplyAsync(() -> readStream(process.getInputStream()));

        CompletableFuture<String> stderrFuture = CompletableFuture
                .supplyAsync(() -> readStream(process.getErrorStream()));

        int exitCode = process.waitFor();

        System.out.println("Process finished with exit code: " + exitCode);

        String stdout = stdoutFuture.join();
        String stderr = stderrFuture.join();

        return new CommandResult(
                exitCode,
                stdout,
                stderr);
    }

    private String readStream(InputStream inputStream) {

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line)
                        .append(System.lineSeparator());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.toString();
    }
}