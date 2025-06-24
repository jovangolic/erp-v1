package com.jovan.erp_v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                String securitySchemeName = "Bearer";
                return new OpenAPI()
                                .info(new Info()
                                                .title("ERP System for Storage, Logistics and Accounting - SLAM v1.0")
                                                .version("v0.0.1")
                                                .description("ERP sistem za upravljanje skladištem, logistikom, računovodstvom, prodajom, nabavkama, smenama i korisnicima.")
                                                .termsOfService("https://example.com/terms")
                                                .contact(new Contact()
                                                                .name("Jovan Golić")
                                                                .url("https://github.com/jovangolic")
                                                                .email("jovangolic19@gmail.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("http://springdoc.org")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("Project Wiki (Detaljna tehnička dokumentacija)")
                                                .url("https://github.com/jovangolic/erp-v1/wiki"))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName,
                                                                new SecurityScheme()
                                                                                .name(securitySchemeName)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}
