package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller.UserController;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.dto.UserRequest;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.CREATE_USER_SUCCES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserHandler userHandler;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_debeRetornarResponseEntityConStatusCreated() {

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

        //when(modelMapper.map(eq(request), eq(UserDTO.class))).thenReturn(userDTO);


        ResponseEntity<GenericResponseDTO<Void>> response = userController.createUser(request, authenticatedUser);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CREATE_USER_SUCCES.getMessage(), response.getBody().getMessage());
        verify(userHandler).crearUserPropietario(userDTO, "ADMIN");

    }

}

