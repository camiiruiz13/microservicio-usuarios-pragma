package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

public class SwaggerConstants {

    private SwaggerConstants() {
    }

    public static final String TAG_NAME = "Usuarios";
    public static final String TAG_NAME_LOGIN = "Login";
    public static final String TAG_DESCRIPTION = "Operaciones relacionadas con usuarios";
    public static final String TAG_DESCRIPTION_LOGIN = "Operacion para inicio de sesón";

    public static final String CREATE_USER_SUMMARY = "Crear un usuario dependiendo con los roles";
    public static final String CREATE_USER_CLIENT_SUMMARY = "Crear un usuario cliente";
    public static final String FIND_USER_SUMMARY = "Busca el usuario por correo";
    public static final String FIND_ID_SUMMARY = "Busca el usuario por el id correspondiente";
    public static final String FIND_ID_TRACE_SUMMARY = "Obtengo datos del cliente y empleado";
    public static final String FIND_ID_DESCRIPTION = "Busca el usuario por el id correspondiente para consumir servicios de trazabilidad";

    public static final String FIND_USER_ID_SUMMARY = "Busca el usuario por el id";
    public static final String FIND_USER_ID_TRACE_SUMMARY = "Busca usuarios por el id cliente y empleado";
    public static final String LOGIN_SUMMARY = "Inicia sesion";
    public static final String CREATE_USER_DESCRIPTION = "Permite al rol correspondiente registrar un nuevo usuario segun sus criterios";
    public static final String FIND_USER_DESCRIPTION = "Filtra los usuarios por el correo correspondiente";
    public static final String LOGIN_DESCRIPTION = "Permite al usuario iniciar sesion";
    public static final String CREATE_USER_DESCRIPTION_REQUEST = "Estructura esperada para el campo `request`";


    public static final String RESPONSE_200 = "Inicio de sesion exitoso";
    public static final String RESPONSE_201 = "Usuario Creado correctamente";
    public static final String RESPONSE_400 = "Datos inválidos o incompletos";
    public static final String RESPONSE_401 = "Usuario no autorizado para inicio de sesion";
    public static final String RESPONSE_409 = "Conflicto, usuario ya existente";
    public static final String RESPONSE_500 = "Error interno";

    public static final String HTTP_200 = "200";
    public static final String HTTP_201 = "201";
    public static final String HTTP_400 = "400";
    public static final String HTTP_401 = "401";
    public static final String HTTP_409 = "409";
    public static final String HTTP_500 = "500";

}
