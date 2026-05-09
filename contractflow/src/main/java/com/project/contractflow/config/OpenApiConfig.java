package com.project.contractflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("ContractFlow API")
                        .version("1.0")
                        .description("Backend system for contract management and document querying")
                        .contact(new Contact()
                                .name("Parth Katariya")
                                .email("parth.katariya87@gmail.com")
                        )
                );
    }
}