package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.TokenInvalidException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.impl.UserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.util.RoleCode;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth.AuthenticationFilter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserRequestDTOMapper;


import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.UserMessagesException.TOKEN_INVALID;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.io.IOException;


@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;


    @Mock
    private GenericResponseDTO<?> genericResponseDTO;


    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserRequestDTOMapper userRequestDTOMapper;



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


        userHandler.crearUserPropietario(userDTO, String.valueOf(RoleCode.ADMIN));


        verify(userRequestDTOMapper).toUser(userDTO);
        verify(userServicePort).crearUserPropietario(user, String.valueOf(RoleCode.ADMIN));
    }

    @Test
    void login_returnsGenericResponseDTO_onSuccess() throws IOException {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setCorreo("test@example.com");
        loginDTO.setClave("password");

        User user = new User();
        when(userRequestDTOMapper.toUser(any(UserDTO.class))).thenReturn(user);

        try (MockedConstruction<AuthenticationFilter> mocked = mockConstruction(AuthenticationFilter.class,
                (mock, context) -> {
                    when(mock.authenticateUser(user)).thenReturn(authentication);
                    when(mock.generateTokenResponse(authentication)).thenReturn(genericResponseDTO);
                })) {

            GenericResponseDTO<?> response = userHandler.login(loginDTO);
            assertEquals(genericResponseDTO, response);
        }
    }


    @Test
    void login_throwsTokenInvalidException_onIOException() throws IOException {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setCorreo("test@example.com");
        loginDTO.setClave("password");

        User user = new User();
        when(userRequestDTOMapper.toUser(any(UserDTO.class))).thenReturn(user);

        try (MockedConstruction<AuthenticationFilter> mocked = mockConstruction(AuthenticationFilter.class,
                (mock, context) -> {
                    when(mock.authenticateUser(user)).thenReturn(authentication);
                    when(mock.generateTokenResponse(authentication)).thenThrow(new IOException("IO Error"));
                })) {

            TokenInvalidException exception = assertThrows(TokenInvalidException.class, () -> userHandler.login(loginDTO));
            assertTrue(exception.getMessage().contains(TOKEN_INVALID.getMessage()));
        }
    }


}
