package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.oauth2.client.registration.google")
public class GoogleOAuthProperties {

    private String clientId;
    private String clientSecret;
    private String tokenExchangeEndpoint;
    private String scope;
    private String redirectUrl;
    private String oAuthServerEndpoint;

}
