package com.retoplazoleta.ccamilo.com.microserviciousuarios;


import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserDomainException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserValidationMessage;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IPasswordPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.usecase.UserUseCase;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.RoleCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private  IPasswordPersistencePort passwordPersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(passwordPersistencePort, userPersistencePort);
    }


    @Test
    @Order(1)
    void createUser_debeEjecutarseCorrectamente() {

        User user = buildValidUser();
        user.setClave("clave123");

        Role role = new Role();


        when(passwordPersistencePort.encriptarClave(user.getClave())).thenReturn("clave123Encriptada");
        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(null);
        when(userPersistencePort.getRoleByNombre(RoleCode.PROPIETARIO.name())).thenReturn(role);


        assertDoesNotThrow(() -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
        verify(userPersistencePort).saveUser(user);
    }


    @Test
    @Order(2)
    void nombreVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNombre("");
        assertThrowsConMensaje(UserValidationMessage.NOMBRE_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(3)
    void apellidoVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setApellido(" ");
        assertThrowsConMensaje(UserValidationMessage.APELLIDO_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(4)
    void documentoVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNumeroDocumento(" ");
        assertThrowsConMensaje(UserValidationMessage.DOCUMENTO_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(5)
    void documentoNoNumerico_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNumeroDocumento("abc123");
        assertThrowsConMensaje(UserValidationMessage.DOCUMENTO_NO_NUMERICO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }



    @Test
    @Order(6)
    void celularVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCelular(" ");
        assertThrowsConMensaje(UserValidationMessage.CELULAR_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(7)
    void celularInvalido_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCelular("abc");
        assertThrowsConMensaje(UserValidationMessage.CELULAR_INVALIDO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(8)
    void correoVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCorreo("");
        assertThrowsConMensaje(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(9)
    void claveVacia_lanzaExcepcion() {
        User user = buildValidUser();
        user.setClave("");
        assertThrowsConMensaje(UserValidationMessage.CLAVE.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(10)
    void correoYaExiste_lanzaExcepcion() {
        User user = buildValidUser();

        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(new User());

        assertThrowsConMensaje(UserValidationMessage.CORREO_REGISTRADO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(11)
    void correoInvalido_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCorreo("invalido@");
        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(null);

        assertThrowsConMensaje(UserValidationMessage.CORREO_INVALIDO.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(12)
    void fechaNacimientoNula_lanzaExcepcion() {
        User user = buildValidUser();
        user.setFechaNacimiento(null);
        assertThrowsConMensaje(UserValidationMessage.FECHA_NACIMIENTO_OBLIGATORIA.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(13)
    void menorDeEdad_lanzaExcepcion() {
        User user = buildValidUser();
        user.setFechaNacimiento(LocalDate.now().minusYears(17));
        assertThrowsConMensaje(UserValidationMessage.NO_MAYOR_DE_EDAD.getMensaje(),
                () -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(14)
    void loginCorrecto_retornaUsuario() {
        User user = buildValidUser();
        user.setClave("12345");

        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(user);
        when(passwordPersistencePort.esClaveValida("12345", "12345")).thenReturn(true);

        User result = userUseCase.login(user.getCorreo(), "12345");
        assertEquals(user, result);
    }

    @Test
    @Order(15)
    void loginCorreoVacio_lanzaExcepcion() {
        assertThrowsConMensaje(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje(),
                () -> userUseCase.login("", "clave123"));
    }

    @Test
    @Order(16)
    void loginCorreoInvalido_lanzaExcepcion() {
        assertThrowsConMensaje(UserValidationMessage.CORREO_INVALIDO.getMensaje(),
                () -> userUseCase.login("correo@", "clave123"));
    }

    @Test
    @Order(17)
    void loginClaveVacia_lanzaExcepcion() {
        assertThrowsConMensaje(UserValidationMessage.CLAVE.getMensaje(),
                () -> userUseCase.login("test@correo.com", ""));
    }

    @Test
    @Order(18)
    void loginUsuarioNoEncontrado_lanzaExcepcion() {
        when(userPersistencePort.getUsuarioByCorreo("no@existe.com")).thenReturn(null);

        assertThrowsConMensaje(UserValidationMessage.NO_DATA_FOUND.getMensaje(),
                () -> userUseCase.login("no@existe.com", "clave123"));
    }

    @Test
    @Order(19)
    void getRoleByNombre_null_retornaCliente() {
        Role cliente = new Role();
        when(userPersistencePort.getRoleByNombre(RoleCode.CLIENTE.name())).thenReturn(cliente);

        User user = buildValidUser();
        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(null);

        assertDoesNotThrow(() -> userUseCase.createUser(user, null));
        verify(userPersistencePort).getRoleByNombre(RoleCode.CLIENTE.name());
    }


    @Test
    @Order(20)
    void getRoleByNombre_admin_retornaPropietario() {
        Role propietario = new Role();
        when(userPersistencePort.getUsuarioByNumeroDocumento(anyString())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(anyString())).thenReturn(null);
        when(userPersistencePort.getRoleByNombre(RoleCode.PROPIETARIO.name())).thenReturn(propietario);
        User user = buildValidUser();

        assertDoesNotThrow(() -> userUseCase.createUser(user, RoleCode.ADMIN.name()));
    }

    @Test
    @Order(21)
    void getRoleByNombre_invalido_lanzaExcepcion() {
        assertThrowsConMensaje(UserValidationMessage.ROLE_INVALIDO.getMensaje(),
                () -> userUseCase.createUser(buildValidUser(), "INVALIDO"));
    }

    @Test
    @Order(22)
    void createUser_sinRol_llamaCreateUserConNull() {
        User user = buildValidUser();
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(null);
        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getRoleByNombre(RoleCode.CLIENTE.name())).thenReturn(new Role());
        when(passwordPersistencePort.encriptarClave(anyString())).thenReturn("encrypted");
        assertDoesNotThrow(() -> userUseCase.createUser(user));
        verify(userPersistencePort).saveUser(user);
    }

    @Test
    @Order(23)
    void nombreVacioClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNombre("");
        assertThrowsConMensaje(UserValidationMessage.NOMBRE_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(24)
    void apellidoVacioClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setApellido(" ");
        assertThrowsConMensaje(UserValidationMessage.APELLIDO_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(25)
    void documentoVacioClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNumeroDocumento(" ");
        assertThrowsConMensaje(UserValidationMessage.DOCUMENTO_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(26)
    void documentoNoNumericoClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNumeroDocumento("abc123");
        assertThrowsConMensaje(UserValidationMessage.DOCUMENTO_NO_NUMERICO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }



    @Test
    @Order(27)
    void celularVacioClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCelular(" ");
        assertThrowsConMensaje(UserValidationMessage.CELULAR_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(28)
    void celularInvalidoClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCelular("abc");
        assertThrowsConMensaje(UserValidationMessage.CELULAR_INVALIDO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(29)
    void correoVacioClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCorreo("");
        assertThrowsConMensaje(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(30)
    void claveVaciaClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setClave("");
        assertThrowsConMensaje(UserValidationMessage.CLAVE.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(31)
    void correoClientYaExiste_lanzaExcepcion() {
        User user = buildValidUser();

        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(new User());

        assertThrowsConMensaje(UserValidationMessage.CORREO_REGISTRADO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(32)
    void correoInvalidoClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCorreo("invalido@");
        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(null);

        assertThrowsConMensaje(UserValidationMessage.CORREO_INVALIDO.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(33)
    void fechaNacimientoNulaClient_lanzaExcepcion() {
        User user = buildValidUser();
        user.setFechaNacimiento(null);
        assertThrowsConMensaje(UserValidationMessage.FECHA_NACIMIENTO_OBLIGATORIA.getMensaje(),
                () -> userUseCase.createUser(user, null));
    }

    @Test
    @Order(34)
    void findByIdUsuer_exitoso() {
        User user = buildValidUser();
        user.setId(1L);
        when(userPersistencePort.getUsuarioById(1L)).thenReturn(user);
        User result = userUseCase.findById(1L);
        assertEquals(user, result);
    }

    @Test
    @Order(35)
    void findByIdUsuer_null() {
        User user = buildValidUser();
        user.setId(1L);
        when(userPersistencePort.getUsuarioById(10L)).thenReturn(null);
        UserDomainException ex = assertThrows(UserDomainException.class, () -> userUseCase.findById(10L));
        assertEquals(UserValidationMessage.NO_DATA_FOUND.getMensaje(), ex.getMessage());
    }


    @Test
    @Order(35)
    void getRoleByNombre_propietario_retornaEmpleado() {
        Role empleado = new Role();
        empleado.setNombre(RoleCode.EMPLEADO.name());
        when(userPersistencePort.getUsuarioByNumeroDocumento(anyString())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(anyString())).thenReturn(null);
        when(userPersistencePort.getRoleByNombre(RoleCode.EMPLEADO.name())).thenReturn(empleado);
        User user = buildValidUser();

        assertDoesNotThrow(() -> userUseCase.createUser(user, RoleCode.PROPIETARIO.name()));
    }

    @Test
    @Order(36)
    void fetchEmployeesAndClients_retornaListaCorrectamente() {
        List<Long> chefs = List.of(1L, 2L);
        List<Long> clients = List.of(3L);

        List<Long> idsEsperados = List.of(1L, 2L, 3L);
        List<String> rolesEsperados = List.of("EMPLEADO", "CLIENTE");

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(3L);

        List<User> mockResponse = List.of(user1, user2);

        when(userPersistencePort.fetchEmployeesAndClients(idsEsperados, rolesEsperados))
                .thenReturn(mockResponse);

        List<User> result = userUseCase.fetchEmployeesAndClients(chefs, clients);

        assertEquals(2, result.size());
    }

    @Test
    @Order(37)
    void fetchEmployeesAndClients_listaVacia_lanzaExcepcion() {
        List<Long> chefs = List.of(1L);
        List<Long> clients = List.of(2L);

        List<Long> ids = List.of(1L, 2L);
        List<String> roles = List.of("EMPLEADO", "CLIENTE");

        when(userPersistencePort.fetchEmployeesAndClients(ids, roles))
                .thenReturn(List.of()); // Simula lista vacía

        UserDomainException ex = assertThrows(UserDomainException.class,
                () -> userUseCase.fetchEmployeesAndClients(chefs, clients));

        assertEquals(UserValidationMessage.NO_DATA_FOUND_USERS.getMensaje(), ex.getMessage());
    }

    @Test
    @Order(38)
    void fetchEmployeesAndClients_null_lanzaExcepcion() {
        List<Long> chefs = List.of(1L);
        List<Long> clients = List.of(2L);

        List<Long> ids = List.of(1L, 2L);
        List<String> roles = List.of("EMPLEADO", "CLIENTE");

        when(userPersistencePort.fetchEmployeesAndClients(ids, roles))
                .thenReturn(null);

        UserDomainException ex = assertThrows(UserDomainException.class,
                () -> userUseCase.fetchEmployeesAndClients(chefs, clients));

        assertEquals(UserValidationMessage.NO_DATA_FOUND_USERS.getMensaje(), ex.getMessage());
    }





    private User buildValidUser() {
        User user = new User();
        user.setNombre("Test");
        user.setApellido("Test");
        user.setNumeroDocumento("123456");
        user.setCelular("+573001112233");
        user.setCorreo("test@correo.com");
        user.setClave("12345");
        user.setFechaNacimiento(LocalDate.now().minusYears(20));
        return user;
    }

    private void assertThrowsConMensaje(String mensajeEsperado, Executable executable) {
        UserDomainException exception = assertThrows(UserDomainException.class, executable);
        assertEquals(mensajeEsperado, exception.getMessage());
    }
}



