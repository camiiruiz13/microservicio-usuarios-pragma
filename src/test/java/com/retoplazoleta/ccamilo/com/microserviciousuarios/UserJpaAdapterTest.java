package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.RoleCode;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.adapter.UserJpaAdapter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.RoleEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.UserEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.RoleCode.PROPIETARIO;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;


    @Test
    @Order(1)
    void saveUser_conRolAdmin_asignaPropietario() {
        User user = new User();
        user.setId(1L);
        user.setNombre("Test");
        user.setApellido("Test");
        user.setNumeroDocumento("123456");
        user.setCorreo("correo@test.com");
        user.setClave("123456");
        user.setFechaNacimiento(LocalDate.now().minusYears(20));

        UserEntity userEntity = new UserEntity();
        userEntity.setClave("123456");

        RoleEntity rolPropietario = new RoleEntity(2L, RoleCode.PROPIETARIO.name(), "rol propietario");
        userEntity.setRol(rolPropietario);

        when(userEntityMapper.toUserEntity(user)).thenReturn(userEntity);
        when(roleRepository.findByNombre(String.valueOf(PROPIETARIO))).thenReturn(rolPropietario);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(user);

        User result = userJpaAdapter.saveUser(user, String.valueOf(RoleCode.ADMIN));

        assertNotNull(result);
        verify(userEntityMapper).toUserEntity(user);
        verify(roleRepository).findByNombre(String.valueOf(PROPIETARIO));
        verify(userRepository).save(userEntity);
    }


    @Test
    @Order(2)
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
    @Order(3)
    void getUsuarioByCorreo_debeLanzarExcepcionCuandoNoExiste() {
        String correo = "noexiste@correo.com";
        when(userRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        User result = userJpaAdapter.getUsuarioByCorreo(correo);

        assertNull(result);
        verify(userRepository).findByCorreo(correo);
        verifyNoInteractions(userEntityMapper);
    }

    @Test
    @Order(4)
    void getUsuarioByNumeroDocumento_existente_retornaUsuario() {

        String numeroDEocumento = "12589";
        UserEntity userEntity = new UserEntity();
        User user = new User();

        when(userRepository.findByNumeroDocumento(numeroDEocumento)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(user);

        User resultado = userJpaAdapter.getUsuarioByNumeroDocumento(numeroDEocumento);

        assertNotNull(resultado);
        verify(userRepository).findByNumeroDocumento(numeroDEocumento);
        verify(userEntityMapper).toUserModel(userEntity);


    }

    @Test
    @Order(5)
    void getUsuarioByNumeroDocumento_noExistente_retornaNull() {
        when(userRepository.findByNumeroDocumento("123456")).thenReturn(Optional.empty());

        User result = userJpaAdapter.getUsuarioByNumeroDocumento("123456");

        assertNull(result);
    }




}
