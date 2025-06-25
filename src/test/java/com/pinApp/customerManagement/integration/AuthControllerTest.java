package com.pinApp.customerManagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinApp.customerManagement.model.AuthRequest;
import com.pinApp.customerManagement.service.impl.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    private final String VALID_USERNAME = "testuser";
    private final String VALID_PASSWORD = "testpass";
    private final String INVALID_PASSWORD = "wrongpass";
    private final String MOCK_TOKEN = "mock.token.value";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void whenValidCredentials_thenReturnToken() throws Exception {
        // Configurar usuario mock
        UserDetails userDetails = new User(
                VALID_USERNAME,
                VALID_PASSWORD,
                Collections.emptyList()
        );

        // Configurar autenticación mock
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // Mockear comportamiento
        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        VALID_USERNAME,
                        VALID_PASSWORD
                )
        )).thenReturn(auth);

        Mockito.when(jwtService.generateToken(userDetails))
                .thenReturn(MOCK_TOKEN);

        // Crear request
        AuthRequest request = new AuthRequest(VALID_USERNAME, VALID_PASSWORD);

        // Ejecutar y verificar
        mockMvc.perform(post("/auth/login")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(MOCK_TOKEN));
    }

    @Test
    void whenInvalidCredentials_thenReturnUnauthorized() throws Exception {
        // Mockear excepción de credenciales inválidas
        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        VALID_USERNAME,
                        INVALID_PASSWORD
                )
        )).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Crear request
        AuthRequest request = new AuthRequest(VALID_USERNAME, INVALID_PASSWORD);

        // Ejecutar y verificar
        mockMvc.perform(post("/auth/login")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Usuario o contraseña incorrectos"));
    }

}
