package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.usecase;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;

import static ch.qos.logback.core.util.StringUtil.isNullOrEmpty;


@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    @Override
    public void crearUserPropietario(User user, String role) {

    }

    private void validateUserFields(User user) {
        if (isNullOrEmpty(user.getNombre()) ) {
            throw new UsuariosDomainException("El campo nombre es obligatorio");
        }

        if(isNullOrEmpty(user.getApellido())){
            throw new UsuariosDomainException("El campo nombre es obligatorio");
        }

        if (isNullOrEmpty(user.getNumeroDocumento()) ){
            throw new UsuariosDomainException("El campo numero de documento es obligatorio");
        }

        if (!isNumeric(user.getNumeroDocumento())) {
            throw new UsuariosDomainException("El documento de identidad debe ser numérico");
        }


        if (isNullOrEmpty(user.getCelular()) ){
            throw new UsuariosDomainException("El campo celular es obligatorio");
        }

        if (isNullOrEmpty(user.getCorreo())) {
            throw new UsuariosDomainException("El correo es obligatorio");
        }

        if (!isValidEmail(user.getCorreo())) {
            throw new UsuariosDomainException("Correo no tiene una estructura válida");
        }

        if (!isValidPhone(user.getCelular())) {
            throw new UsuariosDomainException("Celular no tiene un formato válido");
        }


        if (user.getFechaNacimiento() == null) {
            throw new UsuariosDomainException("La fecha de nacimiento es obligatoria");
        }

        if (!isAdult(user.getFechaNacimiento())) {
            throw new UsuariosDomainException("El usuario debe ser mayor de edad");
        }
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
