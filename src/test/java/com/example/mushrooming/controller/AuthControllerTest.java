package com.example.mushrooming.controller;

import com.example.mushrooming.DBTestConfig;
import com.example.mushrooming.component.JwtAuthenticationFilter;
import com.example.mushrooming.dto.RegisterRequest;
import com.example.mushrooming.exception.TokenNotFoundException;
import com.example.mushrooming.exception.UserNotFoundException;
import com.example.mushrooming.model.User;
import com.example.mushrooming.model.VerificationToken;
import com.example.mushrooming.repository.UserRepository;
import com.example.mushrooming.repository.VerificarionTokenRepository;
import com.example.mushrooming.service.MailService;
import com.example.mushrooming.service.RefreshTokenService;
import com.example.mushrooming.util.ServletUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends DBTestConfig {

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private RefreshTokenService tokenService;
    @MockBean
    private ServletUtil servletUtil;
    @MockBean
    private MailService mailService;

    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;
    @Autowired
    private VerificarionTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Transactional
    @Test
    void register() throws Exception {

        RegisterRequest request = RegisterRequest.builder()
                .name("tester")
                .surname("tester")
                .email("test@email.com")
                .login("test")
                .password("hardPass123!")
                .build();

        Mockito.doNothing().when(mailService).sendMail(Mockito.any());
        Mockito.when(servletUtil.getBaseUrl()).thenReturn("http://test.com");

        mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().is(200));

        mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().is(409));

        User user = userRepository.findByLogin(request.getLogin()).orElseThrow(UserNotFoundException::new);
        VerificationToken token = tokenRepository.findByUser(user).orElseThrow(TokenNotFoundException::new);

        mockMvc.perform(get("/api/auth/verify/" + token.getToken()))
                .andExpect(status().is(200))
                .andExpect(content().string("Account activated!! You can use your account now."));

        userRepository.delete(user);
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}