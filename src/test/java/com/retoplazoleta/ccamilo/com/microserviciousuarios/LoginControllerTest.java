package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller.LoginController;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth.AuthenticationFilter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private LoginController loginController;

    @Test
    @Order(1)
    void login_ShouldReturnOkResponse() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setCorreo("correo@ejemplo.com");
        loginDTO.setClave("123456");

        UserDTOResponse userResponse = new UserDTOResponse();
        userResponse.setCorreo("correo@ejemplo.com");

        when(userHandler.login(any(LoginDTO.class))).thenReturn(userResponse);

        Authentication authenticationMock = mock(Authentication.class);

        Map<String, Object> tokenMap = Map.of(
                "token", "mock-token",
                "username", "correo@ejemplo.com"
        );

        try (MockedConstruction<AuthenticationFilter> mocked = mockConstruction(AuthenticationFilter.class,
                (mock, context) -> {
                    when(mock.authenticateUser(any(LoginDTO.class))).thenReturn(authenticationMock);
                    when(mock.generateTokenResponse(authenticationMock)).thenReturn(tokenMap);
                })) {

            // Act
            ResponseEntity<GenericResponseDTO<Map<String, Object>>> response = loginController.login(loginDTO);

            // Assert
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getObjectResponse());
            assertTrue(response.getBody().getObjectResponse().containsKey("token"));

            verify(userHandler).login(loginDTO);
        }
    }

}

