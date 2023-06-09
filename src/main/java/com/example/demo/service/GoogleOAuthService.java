package com.example.demo.service;

import com.example.demo.config.GoogleOAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final GoogleOAuthProperties googleOAuthProperties;

    public String generateOAuthRequestUri(final String scope, final String redirectUrl, final String codeChallenge) {

        return UriComponentsBuilder.fromUriString(googleOAuthProperties.getOAuthServerEndpoint())
                .queryParam("client_id", googleOAuthProperties.getClientId())
                .queryParam("redirect_uri", redirectUrl)
                .queryParam("response_type", "code")
                .queryParam("scope", scope)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .queryParam("access_type", "offline")
                .toUriString();
    }
}
