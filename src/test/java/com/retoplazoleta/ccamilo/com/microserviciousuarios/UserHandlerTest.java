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

import java.util.List;

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
    void createUser_debeLlamarAlUseCaseConUsuarioMapeado() {

        UserDTO userDTO = new UserDTO();
        userDTO.setNombre("Juan");
        userDTO.setApellido("Perez");
        userDTO.setNumeroDocumento("123456");
        userDTO.setCelular("+573001112233");
        userDTO.setCorreo("correo@correo.com");
        userDTO.setFechaNacimiento("02/06/2000");

        User user = new User();
        when(userRequestDTOMapper.toUser(userDTO)).thenReturn(user);
        userHandler.createUser(userDTO, RoleCode.ADMIN.name());

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

    @Test
    void findById_debeRetornarUserDTOResponse() {
        Long id = 1L;
        User user = new User();
        UserDTOResponse expectedResponse = new UserDTOResponse();
        when(userServicePort.findById(id)).thenReturn(user);
        when(userResponseDTOMapper.toDto(user)).thenReturn(expectedResponse);
        UserDTOResponse actualResponse = userHandler.findById(id);
        assertEquals(expectedResponse, actualResponse);
        verify(userServicePort).findById(id);
        verify(userResponseDTOMapper).toDto(user);
    }

    @Test
    void createUserCliaeante_debeLlamarAlUseCaseConUsuarioMapeado() {

        UserDTO userDTO = new UserDTO();
        userDTO.setNombre("Juan");
        userDTO.setApellido("Perez");
        userDTO.setNumeroDocumento("123456");
        userDTO.setCelular("+573001112233");
        userDTO.setCorreo("correo@correo.com");
        userDTO.setFechaNacimiento("02/06/2000");
        User user = new User();
        when(userRequestDTOMapper.toUser(userDTO)).thenReturn(user);
        userHandler.createUser(userDTO);
        verify(userServicePort).createUser(user, null);
    }
    @Test
    void fetchEmployeesAndClients() {

        List<Long> idsEmpleados = List.of(1L, 2L);
        List<Long> idsClientes = List.of(3L);

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(3L);

        List<User> dominioUsuarios = List.of(user1, user2);

        UserDTOResponse dto1 = new UserDTOResponse();
        UserDTOResponse dto2 = new UserDTOResponse();
        List<UserDTOResponse> dtoList = List.of(dto1, dto2);

        when(userServicePort.fetchEmployeesAndClients(idsEmpleados, idsClientes)).thenReturn(dominioUsuarios);
        when(userResponseDTOMapper.toDtoList(dominioUsuarios)).thenReturn(dtoList);

        List<UserDTOResponse> result = userHandler.fetchEmployeesAndClients(idsEmpleados, idsClientes);
        assertNotNull(result);
    }
}
