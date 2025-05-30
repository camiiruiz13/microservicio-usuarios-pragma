package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorException {

    CORREO_NO_EXIST("Correo no encontrado"),
    ERROR_CREDENCIALES("Error al leer las credenciales del usuario");

    private final String message;
}
