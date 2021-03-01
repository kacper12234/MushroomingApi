package com.example.mushrooming.controller;

import com.example.mushrooming.dto.AuthenticationResponse;
import com.example.mushrooming.dto.LoginRequest;
import com.example.mushrooming.dto.RefreshTokenRequest;
import com.example.mushrooming.dto.RegisterRequest;
import com.example.mushrooming.exception.EmailAlreadyUsedException;
import com.example.mushrooming.exception.LoginAlreadyUsedException;
import com.example.mushrooming.service.AuthService;
import com.example.mushrooming.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody RegisterRequest registerRequest) {
        if (authService.emailAlreadyUsed(registerRequest.getEmail()))
            throw new EmailAlreadyUsedException();
        else if (authService.loginAlreadyUsed(registerRequest.getLogin()))
            throw new LoginAlreadyUsedException();
        else {
            authService.signUp(registerRequest);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated!! You can use your account now.", HttpStatus.OK);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return new ResponseEntity(HttpStatus.OK);
    }
}
