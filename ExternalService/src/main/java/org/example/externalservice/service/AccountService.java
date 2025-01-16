package org.example.externalservice.service;


import lombok.extern.log4j.Log4j2;
import org.example.externalservice.dto.UserDto;
import org.example.externalservice.dto.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Log4j2
@Service
public class AccountService {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    @Value("${keycloak.admin-user-url}")
    private String adminUserUrl;
    @Autowired
    private RestClient restClient;
    @Autowired
    private TokenService tokenService;

    public void registerUser(UserDto userDto) {
        String token = tokenService.obtainAdminAccessToken();

        UserRepresentation user = UserRepresentation.from(userDto, "user");
        registerNewUserInKeycloak(token, user);
    }


    private void registerNewUserInKeycloak(final String accessToken, final UserRepresentation userRepresentation) {
        restClient.post()
                .uri(adminUserUrl)
                .header(AUTHORIZATION, BEARER + accessToken)
                .body(userRepresentation)
                .retrieve()
                .toBodilessEntity();
    }

}
