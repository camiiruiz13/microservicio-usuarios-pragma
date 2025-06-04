package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserValidationMessage {

    NOMBRE_OBLIGATORIO("El campo nombre es obligatorio"),
    APELLIDO_OBLIGATORIO("El campo apellido es obligatorio"),
    DOCUMENTO_OBLIGATORIO("El campo número de documento es obligatorio"),
    DOCUMENTO_NO_NUMERICO("El documento de identidad debe ser numérico"),
    CORREO_REGISTRADO("El correo ya está registrado"),
    NUMERODOC_REGISTRADO("El documento ya está registrado"),
    CELULAR_OBLIGATORIO("El campo celular es obligatorio"),
    CORREO_OBLIGATORIO("El correo es obligatorio"),
    CORREO_INVALIDO("Correo no tiene una estructura válida"),
    CELULAR_INVALIDO("Celular no tiene un formato válido"),
    CLAVE("El campo clave  es obligatorio"),
    FECHA_NACIMIENTO_OBLIGATORIA("La fecha de nacimiento es obligatoria"),
    NO_MAYOR_DE_EDAD("El usuario debe ser mayor de edad"),
    NO_DATA_FOUND("Usuario no encontrado"),
    ROLE_INVALIDO("Rol no existen en el sistema");

    private final String mensaje;
}