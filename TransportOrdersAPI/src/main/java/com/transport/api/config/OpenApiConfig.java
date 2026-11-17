package com.transport.api.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;
@Configuration public class OpenApiConfig {
 @Bean OpenAPI openApi(){return new OpenAPI().info(new Info().title("Transport Orders API").version("1.0.0")).addSecurityItem(new SecurityRequirement().addList("bearerAuth")).components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));}
}
