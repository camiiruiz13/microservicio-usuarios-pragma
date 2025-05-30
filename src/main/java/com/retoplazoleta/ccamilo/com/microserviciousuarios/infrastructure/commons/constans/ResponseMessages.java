package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessages {

    SESSION_SUCCES("Inicio de sesion exitoso"),
    TOKEN_INVALIDO("El token JWT es invalido!");

    private final String message;
}
