package com.example.mushrooming.service;

import com.example.mushrooming.dto.AuthenticationResponse;
import com.example.mushrooming.dto.LoginRequest;
import com.example.mushrooming.dto.RefreshTokenRequest;
import com.example.mushrooming.dto.RegisterRequest;
import com.example.mushrooming.exception.UserNotFoundException;
import com.example.mushrooming.exception.VisitNotFoundException;
import com.example.mushrooming.mapper.UserMapper;
import com.example.mushrooming.model.NotificationEmail;
import com.example.mushrooming.model.User;
import com.example.mushrooming.model.VerificationToken;
import com.example.mushrooming.repository.UserRepository;
import com.example.mushrooming.repository.VerificarionTokenRepository;
import com.example.mushrooming.util.ServletUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final VerificarionTokenRepository verificarionTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final RefreshTokenService tokenService;
    private final UserMapper userMapper;
    private final ServletUtil servletUtil;

    @Transactional
    public void signUp(RegisterRequest registerRequest){
        User user = userMapper.mapRequestToUser(registerRequest);
        userRepository.save(user);
        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for signing up to Mushrooming, please click on the below url to activate your account : "
                + servletUtil.getBaseUrl() + "/api/auth/accountVerification/" + token);
        mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(), message));
    }

    @Transactional
    public boolean loginAlreadyUsed(String login){
        return userRepository.findByLogin(login).isPresent();
    }

    @Transactional
    public boolean emailAlreadyUsed(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificarionTokenRepository.save(verificationToken);
        return token;
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authenticationToken = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
        .authenticationToken(authenticationToken)
        .refreshToken(tokenService.generateRefreshToken().getToken())
        .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
        .id(getCurrentUser().getId())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        tokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithLogin(refreshTokenRequest.getLogin());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .id(getCurrentUser().getId())
                .build();
    }

    public void verifyAccount(String token){
        Optional<VerificationToken> verificationTokenOptional = verificarionTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new VisitNotFoundException("Invalid token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

   @Transactional
   void fetchUserAndEnable(VerificationToken verificationToken){
        String login = verificationToken.getUser().getLogin();
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        user.setEnabled(true);
        userRepository.save(user);
   }

   @Transactional(readOnly = true)
    User getCurrentUser(){
       org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return userRepository.findByLogin(principal.getUsername()).orElseThrow(UserNotFoundException::new);
   }
}
