package com.fiap.cleanarchitecture.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da documentação OpenAPI exposta via Swagger UI.
 * <p>
 * Interface gráfica em {@code /swagger-ui.html} e especificação em {@code /v3/api-docs}.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Tech Challenge - API de Restaurantes")
                .description("API para gestão de usuários, tipos de usuário, restaurantes e itens de cardápio.")
                .version("v1"));
    }
}
