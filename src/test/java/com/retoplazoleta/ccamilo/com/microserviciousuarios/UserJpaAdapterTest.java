package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception.NoDataFoundException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.adapter.UserJpaAdapter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.UserEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.CORREO_NO_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserJpaAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    @Test
    @Order(1)
    void getUsuarioByCorreo_debeRetornarUsuarioCuandoExiste() {

        String correo = "usuario@correo.com";
        UserEntity userEntity = new UserEntity();
        User user = new User();

        when(userRepository.findByCorreo(correo)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(user);

        User resultado = userJpaAdapter.getUsuarioByCorreo(correo);

        assertNotNull(resultado);
        verify(userRepository).findByCorreo(correo);
        verify(userEntityMapper).toUserModel(userEntity);


    }

    @Test
    @Order(2)
    void getUsuarioByCorreo_debeLanzarExcepcionCuandoNoExiste() {
        String correo = "noexiste@correo.com";
        when(userRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(NoDataFoundException.class, () ->
                userJpaAdapter.getUsuarioByCorreo(correo)
        );

        assertEquals(CORREO_NO_EXIST.getMessage(), exception.getMessage());
        verify(userRepository).findByCorreo(correo);
        verifyNoInteractions(userEntityMapper);
    }

}
