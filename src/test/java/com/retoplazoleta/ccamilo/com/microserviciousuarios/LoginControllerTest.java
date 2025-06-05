package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller.LoginController;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private LoginController loginController;

    @Test
    void login_ShouldReturnOkResponse() {

        LoginDTO loginDTO = new LoginDTO();
        GenericResponseDTO<Void> expectedResponse = new GenericResponseDTO<>();
        when(userHandler.login(any(LoginDTO.class))).thenAnswer(invocation -> new GenericResponseDTO<>());




        ResponseEntity<GenericResponseDTO<Void>> response = loginController.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userHandler).login(loginDTO);
    }
}
