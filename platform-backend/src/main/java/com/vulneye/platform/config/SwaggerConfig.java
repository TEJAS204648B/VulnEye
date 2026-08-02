package com.vulneye.platform.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI vulneyeOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("VulnEye API")
                        .description(
                                "Backend REST API for the VulnEye Vulnerability Assessment and Penetration Testing Platform.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Tejas Chaudhari")
                                .email("your-email@example.com"))
                        .license(new License()
                                .name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("VulnEye Documentation"));
    }
}