package com.fiap.tech_challenge.parte1.ms_users.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI msusuarios(){
        return new OpenAPI()
                .info(new Info()
                        .title("Cadastro de Usuários")
                        .description("Tech Challenge 1 do curso de Pós-Tech Arquitetura Java, da FIAP. Serviço consiste em cadastro de usuários em aplicativo fictício de restaurantes")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("https://github.com/anacarolcortez/tech-challenge-fiap-parte1"))
                );
    }
}
