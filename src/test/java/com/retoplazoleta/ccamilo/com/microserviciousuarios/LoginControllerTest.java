package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller.LoginController;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoginControllerTest {



    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private LoginController loginController;

    @Test
    void login_ShouldReturnOkResponse() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setCorreo("correo@ejemplo.com");
        loginDTO.setClave("123456");

        UserDTOResponse userResponse = new UserDTOResponse();
        userResponse.setCorreo("correo@ejemplo.com");

        when(userHandler.login(any(LoginDTO.class))).thenReturn(userResponse);


        ResponseEntity<GenericResponseDTO<Map<String, Object>>> response = loginController.login(loginDTO);



        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getObjectResponse());
        assertTrue(response.getBody().getObjectResponse().containsKey("token")); // el filtro genera un token

        verify(userHandler).login(loginDTO);
    }
}