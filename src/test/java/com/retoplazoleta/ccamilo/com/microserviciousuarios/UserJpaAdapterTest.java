package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception.AutenticationException;
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

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.ERROR_CREDENCIALES;
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
    void saveUser_debeGuardarUsuarioConClaveEncriptada() {
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

        String claveEncriptada = "$2a$10$abc123"; // ejemplo

        when(userEntityMapper.toUserEntity(user)).thenReturn(userEntity);
        when(passwordEncoder.encode("123456")).thenReturn(claveEncriptada);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(user);

        User result = userJpaAdapter.saveUser(user);

        assertNotNull(result);
        verify(userEntityMapper).toUserEntity(user);
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(userEntity);
        verify(userEntityMapper).toUserModel(userEntity);
        assertEquals(claveEncriptada, userEntity.getClave());
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

    @Test
    @Order(6)
    void getRoleByNombre_existente_retornaRole() {
        String nombre = "PROPIETARIO";
        RoleEntity roleEntity = new RoleEntity(1L, nombre, "desc");
        Role expectedRole = new Role(1L, nombre, "desc");

        when(roleRepository.findByNombre(nombre)).thenReturn(Optional.of(roleEntity));
        when(userEntityMapper.toRoleModel(roleEntity)).thenReturn(expectedRole);

        Role result = userJpaAdapter.getRoleByNombre(nombre);

        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        verify(roleRepository).findByNombre(nombre);
        verify(userEntityMapper).toRoleModel(roleEntity);
    }

    @Test
    @Order(7)
    void getRoleByNombre_noExistente_retornaNull() {
        String nombre = "ROL_INEXISTENTE";
        when(roleRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        Role result = userJpaAdapter.getRoleByNombre(nombre);

        assertNull(result);
        verify(roleRepository).findByNombre(nombre);
        verifyNoInteractions(userEntityMapper);
    }


    @Test
    @Order(8)
    void esClaveValida_claveCorrecta_retornaTrue() {
        String claveIngresada = "12345";
        String claveBD = "$2a$10$abc123";

        when(passwordEncoder.matches(claveIngresada, claveBD)).thenReturn(true);

        boolean resultado = userJpaAdapter.esClaveValida(claveIngresada, claveBD);

        assertTrue(resultado);
        verify(passwordEncoder).matches(claveIngresada, claveBD);
    }


    @Test
    @Order(9)
    void esClaveValida_claveIncorrecta_lanzaExcepcion() {
        String claveIngresada = "12345";
        String claveBD = "$2a$10$abc123";

        when(passwordEncoder.matches(claveIngresada, claveBD)).thenReturn(false);

        AutenticationException exception = assertThrows(AutenticationException.class, () ->
                userJpaAdapter.esClaveValida(claveIngresada, claveBD));

        assertEquals(ERROR_CREDENCIALES.getMessage(), exception.getMessage());
        verify(passwordEncoder).matches(claveIngresada, claveBD);
    }



}
