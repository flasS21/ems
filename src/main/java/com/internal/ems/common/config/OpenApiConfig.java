package com.internal.ems.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI emsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management System (EMS) API")
                        .description("REST API documentation for managing employees and departments.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("EMS Dev Team")
                                .email("dev@internal.ems"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
