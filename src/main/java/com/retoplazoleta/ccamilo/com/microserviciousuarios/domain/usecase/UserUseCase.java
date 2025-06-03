package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.usecase;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserDomainException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserValidationMessage;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserValidationMessage.NUMERODOC_REGISTRADO;


@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    @Override
    public void crearUserPropietario(User user, String role) {
        validateUserFields(user);
        userPersistencePort.saveUser(user, role);

    }

    private void validateUserFields(User user) {

        if (isNullOrEmpty(user.getNombre())) {
            throw new UserDomainException(UserValidationMessage.NOMBRE_OBLIGATORIO.getMensaje());
        }

        if (isNullOrEmpty(user.getApellido())) {
            throw new UserDomainException(UserValidationMessage.APELLIDO_OBLIGATORIO.getMensaje());
        }

        if (isNullOrEmpty(user.getNumeroDocumento())) {
            throw new UserDomainException(UserValidationMessage.DOCUMENTO_OBLIGATORIO.getMensaje());
        }

        if (!isNumeric(user.getNumeroDocumento())) {
            throw new UserDomainException(UserValidationMessage.DOCUMENTO_NO_NUMERICO.getMensaje());
        }

        if (userPersistencePort.getUsuarioByNumeroDocumento(user.getNumeroDocumento()) != null) {
            throw new UserDomainException(NUMERODOC_REGISTRADO.getMensaje());
        }

        if (isNullOrEmpty(user.getCelular())) {
            throw new UserDomainException(UserValidationMessage.CELULAR_OBLIGATORIO.getMensaje());
        }

        if (!isValidPhone(user.getCelular())) {
            throw new UserDomainException(UserValidationMessage.CELULAR_INVALIDO.getMensaje());
        }

        if (isNullOrEmpty(user.getCorreo())) {
            throw new UserDomainException(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje());
        }

        if (userPersistencePort.getUsuarioByCorreo(user.getCorreo()) != null) {
            throw new UserDomainException(UserValidationMessage.CORREO_REGISTRADO.getMensaje());
        }

        if (!isValidEmail(user.getCorreo())) {
            throw new UserDomainException(UserValidationMessage.CORREO_INVALIDO.getMensaje());
        }


        if (user.getFechaNacimiento() == null) {
            throw new UserDomainException(UserValidationMessage.FECHA_NACIMIENTO_OBLIGATORIA.getMensaje());
        }

        if (!isAdult(user.getFechaNacimiento())) {
            throw new UserDomainException(UserValidationMessage.NO_MAYOR_DE_EDAD.getMensaje());
        }
    }

    private Boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private Boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }

    private Boolean isValidPhone(String phone) {
        String regex = "^\\+?[0-9]{1,13}$";
        return Pattern.matches(regex, phone);
    }
    private Boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private boolean isAdult(LocalDate fechaNacimiento) {
        LocalDate today = LocalDate.now();
        return fechaNacimiento.plusYears(18).isBefore(today) || fechaNacimiento.plusYears(18).isEqual(today);
    }


}
