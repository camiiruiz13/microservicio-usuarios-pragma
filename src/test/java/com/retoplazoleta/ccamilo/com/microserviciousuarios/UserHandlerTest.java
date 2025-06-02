package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.impl.UserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserRequestDTOMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.RoleCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserRequestDTOMapper userRequestDTOMapper;

    @InjectMocks
    private UserHandler userHandler;

    @Test
    void crearUserPropietario_debeLlamarAlUseCaseConUsuarioMapeado() {

        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(1L);
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
}
