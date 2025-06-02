package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorException {

    CORREO_NO_EXIST("Correo no encontrado"),
    ACCES_DENIED("Acceso denegado: No tiene permisos para realizar esta operación"),
    INVALID_TOKEN("No autorizado: token inválido o no enviado"),
    ERROR_EXCEPTION("Ha ocurrido un error inesperado"),
    ERROR_CREDENCIALES("Error al leer las credenciales del usuario");

    private final String message;
}
