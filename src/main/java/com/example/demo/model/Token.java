package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Token {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private String expiresIn;
    private String idToken;
    private String refreshToken;
    @SerializedName("scope")
    private String scope;
    @SerializedName("token_type")
    private String tokenType;
}
