package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

public class SwaggerConstants {

    private SwaggerConstants() {
    }

    public static final String TAG_NAME = "Usuarios";
    public static final String TAG_DESCRIPTION = "Operaciones relacionadas con usuarios";

    public static final String CREATE_USER_SUMMARY = "Crear un propietario";
    public static final String CREATE_USER_DESCRIPTION = "Permite al ADMIN registrar un nuevo usuario con rol PROPIETARIO";
    public static final String CREATE_USER_DESCRIPTION_REQUEST = "Estructura esperada para el campo `request`";

    public static final String RESPONSE_201 = "Usuario creado correctamente";
    public static final String RESPONSE_400 = "Datos inválidos o incompletos";
    public static final String RESPONSE_409 = "Conflicto, usuario ya existente";
    public static final String RESPONSE_500 = "Error interno";

    public static final String HTTP_201 = "201";
    public static final String HTTP_400 = "400";
    public static final String HTTP_409 = "409";
    public static final String HTTP_500 = "500";

}
