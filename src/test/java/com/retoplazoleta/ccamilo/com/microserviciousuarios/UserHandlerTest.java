package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.impl.UserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserResponseDTOMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.RoleCode;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserRequestDTOMapper;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserRequestDTOMapper userRequestDTOMapper;

    @Mock
    private UserResponseDTOMapper userResponseDTOMapper;

    @InjectMocks
    private UserHandler userHandler;

    @Test
    void crearUserPropietario_debeLlamarAlUseCaseConUsuarioMapeado() {

        UserDTO userDTO = new UserDTO();
        userDTO.setNombre("Juan");
        userDTO.setApellido("Perez");
        userDTO.setNumeroDocumento("123456");
        userDTO.setCelular("+573001112233");
        userDTO.setCorreo("correo@correo.com");
        userDTO.setFechaNacimiento("02/06/2000");

        User user = new User();
        when(userRequestDTOMapper.toUser(userDTO)).thenReturn(user);


        userHandler.crearUserPropietario(userDTO, RoleCode.ADMIN.name());


        verify(userRequestDTOMapper).toUser(userDTO);
        verify(userServicePort).createUser(user, RoleCode.ADMIN.name());
    }

    @Test
    void login_debeRetornarUserDTOResponse() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setCorreo("test@example.com");
        loginDTO.setClave("password");

        User user = new User();
        UserDTOResponse expectedResponse = new UserDTOResponse();

        when(userServicePort.login(loginDTO.getCorreo(), loginDTO.getClave())).thenReturn(user);
        when(userResponseDTOMapper.toDto(user)).thenReturn(expectedResponse);

        UserDTOResponse actualResponse = userHandler.login(loginDTO);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(userServicePort).login(loginDTO.getCorreo(), loginDTO.getClave());
        verify(userResponseDTOMapper).toDto(user);
    }

    @Test
    void findByCorreo_debeRetornarUserDTOResponse() {

        String correo = "correo@correo.com";
        User user = new User();
        UserDTOResponse expectedResponse = new UserDTOResponse();

        when(userServicePort.findByCorreo(correo)).thenReturn(user);
        when(userResponseDTOMapper.toDto(user)).thenReturn(expectedResponse);


        UserDTOResponse actualResponse = userHandler.findByCorreo(correo);


        assertEquals(expectedResponse, actualResponse);
        verify(userServicePort).findByCorreo(correo);
        verify(userResponseDTOMapper).toDto(user);
    }
}
