package com.example.mushrooming.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotNull
    private String refreshToken;
    private String login;
}
