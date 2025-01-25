package com.nand.grievance.publisher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("My Application API")
                        .description("A sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")
                        .contact(new Contact()
                                .name("Nandakishor")
                                .email("nandakishor.m@ascendlearning.com")
                                .url("http://example.com"))
                        .version("1.0.0"));
    }
}

// import org.springdoc.core.GroupedOpenApi;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class OpenApiConfiguration {

//     @Bean
//     public GroupedOpenApi publicApi() {
//         return GroupedOpenApi.builder()
//                 .group("public")
//                 .pathsToMatch("/public/**")
//                 .build();
//     }

//     @Bean
//     public GroupedOpenApi privateApi() {
//         return GroupedOpenApi.builder()
//                 .group("private")
//                 .pathsToMatch("/private/**")
//                 .build();
//     }
// }