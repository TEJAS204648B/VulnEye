package com.vulneye.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PlatformBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformBackendApplication.class, args);
	}

}