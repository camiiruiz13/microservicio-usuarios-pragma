package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserFilterDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller.UserController;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.IdsRequest;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.UserRequest;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.CREATE_USER_SUCCES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private UserController userController;

    @Test
    @Order(1)
    void createUser_debeRetornarResponseEntityConStatusCreatedAdmin() {

        UserRequest request = new UserRequest();


        UserDTO userDTO = new UserDTO();
        userDTO.setNombre("Carlos");
        userDTO.setApellido("Pérez");
        userDTO.setNumeroDocumento("123456789");
        userDTO.setCorreo("carlos@mail.com");
        userDTO.setCelular("+573001112233");
        userDTO.setFechaNacimiento("01/01/2000");
        userDTO.setClave("clave123");
        request.setRequest(userDTO);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                "1", "carlos@mail.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        ResponseEntity<GenericResponseDTO<Void>> response = userController.createUser(request, authenticatedUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CREATE_USER_SUCCES.getMessage(), response.getBody().getMessage());
        verify(userHandler).createUser(userDTO, "ADMIN");

    }

    @Test
    @Order(2)
    void findByCorreo_debeRetornarUsuarioConStatusOk() {

        String correo = "usuario@mail.com";

        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setCorreo(correo);

        when(userHandler.findByCorreo(correo)).thenReturn(userDTOResponse);

        ResponseEntity<GenericResponseDTO<UserDTOResponse>> response = userController.findByCorreo(correo);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(correo, response.getBody().getObjectResponse().getCorreo());

        verify(userHandler).findByCorreo(correo);
    }

    @Test
    @Order(3)
    void createUser_debeRetornarResponseEntityConStatusCreatedEmp() {

        UserRequest request = new UserRequest();



        UserDTO userDTO = new UserDTO();
        userDTO.setNombre("Carlos");
        userDTO.setApellido("Pérez");
        userDTO.setNumeroDocumento("123456789");
        userDTO.setCorreo("carlos@mail.com");
        userDTO.setCelular("+573001112233");
        userDTO.setFechaNacimiento("01/01/2000");
        userDTO.setClave("clave123");
        request.setRequest(userDTO);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                "1", "carlos@mail.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_EMP"))
        );


        ResponseEntity<GenericResponseDTO<Void>> response = userController.createUser(request, authenticatedUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CREATE_USER_SUCCES.getMessage(), response.getBody().getMessage());
        verify(userHandler).createUser(userDTO, "EMP");

    }

    @Test
    @Order(4)
    void createUser_debeRetornarResponseEntityConStatusCreatedCli() {

        UserRequest request = new UserRequest();

        UserDTO userDTO = new UserDTO();
        userDTO.setNombre("Carlos");
        userDTO.setApellido("Pérez");
        userDTO.setNumeroDocumento("123456789");
        userDTO.setCorreo("carlos@mail.com");
        userDTO.setCelular("+573001112233");
        userDTO.setFechaNacimiento("01/01/2000");
        userDTO.setClave("clave123");
        request.setRequest(userDTO);

        ResponseEntity<GenericResponseDTO<Void>> response = userController.createUsertClient(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    @Order(5)
    void findById_debeRetornarUsuarioConStatusOk() {

        Long id = 1L;

        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setIdUsuario(id);

        when(userHandler.findById(id)).thenReturn(userDTOResponse);

        ResponseEntity<GenericResponseDTO<UserDTOResponse>> response = userController.findById(id);

        assertNotNull(response);

    }

    @Test
    @Order(6)
    void findByIdUsers_returnsOkResponse() {

        List<Long> chefIds = List.of(1L, 2L);
        List<Long> clientIds = List.of(3L);

        UserFilterDTO filterDTO = new UserFilterDTO();
        filterDTO.setChefIds(chefIds);
        filterDTO.setClientIds(clientIds);

        IdsRequest request = new IdsRequest();
        request.setRequest(filterDTO);

        UserDTOResponse dto1 = new UserDTOResponse();
        UserDTOResponse dto2 = new UserDTOResponse();
        List<UserDTOResponse> mockResponse = List.of(dto1, dto2);

        when(userHandler.fetchEmployeesAndClients(chefIds, clientIds)).thenReturn(mockResponse);

        ResponseEntity<GenericResponseDTO<List<UserDTOResponse>>> response = userController.findByIdUsers(request);

        assertNotNull(response.getBody());
    }



}

