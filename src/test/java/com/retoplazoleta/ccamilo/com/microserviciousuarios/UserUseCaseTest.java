package com.retoplazoleta.ccamilo.com.microserviciousuarios;


import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserDomainException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserValidationMessage;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.usecase.UserUseCase;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.RoleCode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.function.Executable;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    @Order(1)
    void crearUserPropietario_debeEjecutarseCorrectamente() {
        User user = buildValidUser();

        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(null);

        assertDoesNotThrow(() -> userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));

        verify(userPersistencePort).saveUser(user, String.valueOf(RoleCode.ADMIN));
    }

    @Test
    @Order(2)
    void nombreVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNombre("");
        assertThrowsConMensaje(UserValidationMessage.NOMBRE_OBLIGATORIO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(3)
    void apellidoVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setApellido(" ");
        assertThrowsConMensaje(UserValidationMessage.APELLIDO_OBLIGATORIO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(4)
    void documentoVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNumeroDocumento(" ");
        assertThrowsConMensaje(UserValidationMessage.DOCUMENTO_OBLIGATORIO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(5)
    void documentoNoNumerico_lanzaExcepcion() {
        User user = buildValidUser();
        user.setNumeroDocumento("abc123");
        assertThrowsConMensaje(UserValidationMessage.DOCUMENTO_NO_NUMERICO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(6)
    void documentoYaExiste_lanzaExcepcion() {
        User user = buildValidUser();

        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento()))
                .thenReturn(new User());

        assertThrowsConMensaje(UserValidationMessage.NUMERODOC_REGISTRADO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(7)
    void celularVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCelular(" ");
        assertThrowsConMensaje(UserValidationMessage.CELULAR_OBLIGATORIO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(8)
    void celularInvalido_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCelular("abc");
        assertThrowsConMensaje(UserValidationMessage.CELULAR_INVALIDO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(9)
    void correoVacio_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCorreo("");
        assertThrowsConMensaje(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(10)
    void correoYaExiste_lanzaExcepcion() {
        User user = buildValidUser();
        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(new User());

        assertThrowsConMensaje(UserValidationMessage.CORREO_REGISTRADO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(11)
    void correoInvalido_lanzaExcepcion() {
        User user = buildValidUser();
        user.setCorreo("invalido@");
        when(userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento())).thenReturn(null);
        when(userPersistencePort.getUsuarioByCorreo(user.getCorreo())).thenReturn(null);

        assertThrowsConMensaje(UserValidationMessage.CORREO_INVALIDO.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(12)
    void fechaNacimientoNula_lanzaExcepcion() {
        User user = buildValidUser();
        user.setFechaNacimiento(null);
        assertThrowsConMensaje(UserValidationMessage.FECHA_NACIMIENTO_OBLIGATORIA.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    @Test
    @Order(13)
    void menorDeEdad_lanzaExcepcion() {
        User user = buildValidUser();
        user.setFechaNacimiento(LocalDate.now().minusYears(17));
        assertThrowsConMensaje(UserValidationMessage.NO_MAYOR_DE_EDAD.getMensaje(), () ->
                userUseCase.crearUserPropietario(user, String.valueOf(RoleCode.ADMIN)));
    }

    private User buildValidUser() {
        User user = new User();
        user.setNombre("Test");
        user.setApellido("Test");
        user.setNumeroDocumento("123456");
        user.setCelular("+573001112233");
        user.setCorreo("test@correo.com");
        user.setFechaNacimiento(LocalDate.now().minusYears(20));
        return user;
    }

    private void assertThrowsConMensaje(String mensajeEsperado, Executable executable) {
        UserDomainException exception = assertThrows(UserDomainException.class, executable);
        assertEquals(mensajeEsperado, exception.getMessage());
    }
}


