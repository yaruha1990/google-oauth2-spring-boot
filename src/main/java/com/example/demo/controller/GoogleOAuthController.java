package com.example.demo.controller;

import com.example.demo.config.GoogleOAuthProperties;
import com.example.demo.model.Token;
import com.example.demo.service.GoogleOAuthService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static com.google.common.hash.Hashing.sha256;
import static org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString;

@RestController
@RequiredArgsConstructor
public class GoogleOAuthController {

    private final GoogleOAuthService oAuthService;
    private final RestTemplate restTemplate;
    private final GoogleOAuthProperties googleOAuthProperties;


    @GetMapping(value = "/authorization")
    public ResponseEntity<Void> redirectToOAuthServer(final HttpSession session) {
        final String codeVerifier = UUID.randomUUID().toString();
        final String codeChallenge = encodeBase64URLSafeString(sha256().hashString(codeVerifier, StandardCharsets.UTF_8).asBytes());

        session.setAttribute("codeVerifier", codeVerifier);

        final String oAuthServerUri = oAuthService.generateOAuthRequestUri(googleOAuthProperties.getScope(),
                googleOAuthProperties.getRedirectUrl(), codeChallenge);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(oAuthServerUri)).build();
    }

    @GetMapping("/code")
    public ResponseEntity<Token> exchangeCodeForToken(final String code, final HttpSession session) {

        final String codeVerifier = (String) session.getAttribute("codeVerifier");
        final String tokenInfo = restTemplate.postForObject(googleOAuthProperties.getTokenExchangeEndpoint(), Map.of(
                "client_id", googleOAuthProperties.getClientId(),
                "client_secret", googleOAuthProperties.getClientSecret(),
                "code", code,
                "code_verifier", codeVerifier,
                "grant_type", "authorization_code",
                "redirect_uri", googleOAuthProperties.getRedirectUrl()), String.class);

        final Token token = new Gson().fromJson(tokenInfo, Token.class);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
