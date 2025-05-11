package com.biblioteca.biblioteca_api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

        @Bean
        public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .build();
        }

        @Bean
        public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Librería")
                        .version("1.0.0")
                        .description("Descripción detallada de la API para una librería con usuarios con diferentes roles e informacion de libros y autores.")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .name("Jesús Cacabelos Montero")
                                .url("")
                                .email("jesuscacabelosmontero@gmail.com"))
                        .license(new License()
                                .name("Licencia")
                                .url(""))
                );
        }
}