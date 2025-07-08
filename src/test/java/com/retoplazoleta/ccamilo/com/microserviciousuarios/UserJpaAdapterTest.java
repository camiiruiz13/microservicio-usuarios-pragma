package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.entities.RoleEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.entities.UserEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserJpaAdapterTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;


    @Test
    @Order(1)
    void saveUser_debeGuardarUsuarioConExito() {

        User user = new User();
        user.setCorreo("test@correo.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setCorreo("test@correo.com");

        UserEntity savedEntity = new UserEntity();
        savedEntity.setCorreo("test@correo.com");

        User userFinal = new User();
        userFinal.setCorreo("test@correo.com");

        when(userEntityMapper.toUserEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedEntity);
        when(userEntityMapper.toUserModel(savedEntity)).thenReturn(userFinal);


        User result = userJpaAdapter.saveUser(user);


        assertNotNull(result);
        assertEquals("test@correo.com", result.getCorreo());
        verify(userEntityMapper).toUserEntity(user);
        verify(userRepository).save(userEntity);
        verify(userEntityMapper).toUserModel(savedEntity);
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
    void getUsuarioById_debeRetornarUsuarioCuandoExiste() {

        Long idUser = 1L;
        UserEntity userEntity = new UserEntity();
        User user = new User();

        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(user);

        User resultado = userJpaAdapter.getUsuarioById(idUser);

        assertNotNull(resultado);
        verify(userRepository).findById(idUser);
        verify(userEntityMapper).toUserModel(userEntity);


    }

    @Test
    @Order(9)
    void getUsuarioByIdUser_debeLanzarExcepcionCuandoNoExiste() {
        Long idUser = 100L;
        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        User result = userJpaAdapter.getUsuarioById(idUser);

        assertNull(result);
    }

    @Test
    @Order(10)
    void fetchEmployeesAndClients(){

        List<Long> ids = List.of(1L, 2L);
        List<String> roles = List.of("EMPLEADO", "CLIENTE");

        UserEntity user1 = new UserEntity();
        user1.setId(1L);

        UserEntity user2 = new UserEntity();
        user2.setId(3L);

        List<UserEntity> mockResponse = List.of(user1, user2);

        when(userRepository.fetchEmployeesAndClients(ids, roles))
                .thenReturn(mockResponse);

        List<User> result = userJpaAdapter.fetchEmployeesAndClients(ids, roles);
        assertNotNull(result);
    }


}
