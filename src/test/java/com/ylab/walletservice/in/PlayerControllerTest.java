package com.ylab.walletservice.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.presentation.in.controller.PlayerController;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Player controller tests")
public class PlayerControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private PlayerController playerController;

    @Mock
    private PlayerService playerServiceMock;

    @Mock
    private JwtService jwtServiceMock;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("It should authorize player with valid credentials successfully")
    public void testDoAuthorisationValidCredentials() throws Exception {
        CredentialsDto validCredentials = new CredentialsDto("login", "password");
        LoggedInPlayerDto loggedInPlayerDto = new LoggedInPlayerDto(1, "1");

        when(playerServiceMock.doAuthorisation(validCredentials)).thenReturn(Optional.of(loggedInPlayerDto));
        when(jwtServiceMock.generateToken(loggedInPlayerDto)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/player/authorisation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validCredentials)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));

        verify(playerServiceMock, times(1)).doAuthorisation(validCredentials);
        verify(jwtServiceMock, times(1)).generateToken(loggedInPlayerDto);
    }

    @Test
    @DisplayName("It should fail authorization for invalid credentials")
    public void testDoAuthorisationInvalidCredentials() throws Exception {
        CredentialsDto invalidCredentials = new CredentialsDto("invalidUsername", "invalidPassword");

        when(playerServiceMock.doAuthorisation(invalidCredentials)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/player/authorisation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidCredentials)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authorization failed!"));

        verify(playerServiceMock, times(1)).doAuthorisation(invalidCredentials);
        verifyNoInteractions(jwtServiceMock);
    }

    @Test
    @DisplayName("It should register player with valid data successfully")
    public void testDoRegistrationValidData() throws Exception {
        RegistrationDto validRegistrationData = new RegistrationDto("2", "2");

        when(playerServiceMock.create(validRegistrationData)).thenReturn(true);

        mockMvc.perform(post("/api/player/registration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validRegistrationData)))
                .andExpect(status().isOk())
                .andExpect(content().string("Success registration!"));

        verify(playerServiceMock, times(1)).create(validRegistrationData);
    }

    @Test
    @DisplayName("It should fail registration for existing data")
    public void testDoRegistrationExistingData() throws Exception {
        RegistrationDto invalidRegistrationData = new RegistrationDto("3", "3");

        when(playerServiceMock.create(invalidRegistrationData)).thenReturn(false);

        mockMvc.perform(post("/api/player/registration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidRegistrationData)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Registration failed!"));

        verify(playerServiceMock, times(1)).create(invalidRegistrationData);
    }

    @Test
    @DisplayName("It should retrieve player's balance successfully")
    public void testGetBalance() throws Exception {
        LoggedInPlayerDto loggedInPlayerDto = new LoggedInPlayerDto(1L, "playerLogin");
        when(playerServiceMock.getBalance(loggedInPlayerDto.id())).thenReturn(new BigDecimal(1000));

        MockHttpServletRequestBuilder requestBuilder = get("/api/player/balance")
                .requestAttr("player", loggedInPlayerDto);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("Balance = 1000"));

        verify(playerServiceMock, times(1)).getBalance(loggedInPlayerDto.id());
    }
}