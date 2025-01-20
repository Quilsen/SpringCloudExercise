package org.example.externalservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API with Keycloak Integration",
                version = "1.0",
                description = "API secured with Keycloak and OAuth2"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                implicit = @OAuthFlow(
                        authorizationUrl = "http://localhost:8084/realms/master/protocol/openid-connect/auth",
                        scopes = {
                                @OAuthScope(name = "profile", description = "Access profile information")
                        }
                )
        )
)
public class OpenApiConfig {
}
