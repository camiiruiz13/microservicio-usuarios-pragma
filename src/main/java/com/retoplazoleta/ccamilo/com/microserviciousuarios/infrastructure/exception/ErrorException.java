package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorException {

    CORREO_NO_EXIST("Correo no encontrado");

    private final String message;
}
