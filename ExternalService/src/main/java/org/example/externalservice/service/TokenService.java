package org.example.externalservice.service;

import lombok.extern.log4j.Log4j2;
import org.example.externalservice.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.StringJoiner;

@Log4j2
@Service
public class TokenService {
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${keycloak.admin.password}")
    private String password;

    @Autowired
    private RestClient restClient;

    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpiryTime;
    private Instant refreshTokenExpiryTime;

    public String obtainAdminAccessToken() {
        if (accessToken != null && Instant.now().isBefore(accessTokenExpiryTime)) {
            log.warn("Transfer of an existing token");
            return accessToken;
        }

        if (refreshToken != null && Instant.now().isBefore(refreshTokenExpiryTime)) {
            log.warn("Token renewal using refresh token");
            return refreshAccessToken();
        }
        log.warn("Obtaining new access token");
        return getNewAccessToken();
    }

    private String refreshAccessToken() {
        String formData = new StringJoiner("&")
                .add("grant_type=refresh_token")
                .add("client_id=" + clientId)
                .add("refresh_token=" + refreshToken)
                .toString();

        TokenResponse response = restClient.post()
                .uri(authServerUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<TokenResponse>() {
                })
                .getBody();

        updateTokens(response);

        return response.access_token();
    }

    private String getNewAccessToken() {
        String formData = new StringJoiner("&")
                .add("grant_type=password")
                .add("client_id=" + clientId)
                .add("username=" + username)
                .add("password=" + password)
                .toString();

        TokenResponse response = restClient.post()
                .uri(authServerUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<TokenResponse>() {
                })
                .getBody();

        updateTokens(response);

        return response.access_token();
    }

    private void updateTokens(TokenResponse response) {
        this.accessToken = response.access_token();
        this.refreshToken = response.refresh_token();
        this.accessTokenExpiryTime = Instant.now().plusSeconds(response.expires_in());
        this.refreshTokenExpiryTime = Instant.now().plusSeconds(response.refresh_expires_in());
    }
}

