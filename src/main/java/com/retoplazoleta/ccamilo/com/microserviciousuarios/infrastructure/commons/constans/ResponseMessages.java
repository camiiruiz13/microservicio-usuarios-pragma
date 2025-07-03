package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessages {

    CREATE_USER_SUCCES("Se ha creado el usuario correctamente "),
    FIND_USER_SUCCES("Se filtra el correo exitosamente "),
    FIND_USER_ID_SUCCES("Se filtra el usuario por id  exitosamente "),
    FIND_USERS_ID_SUCCES("Se filtra los usuarios por idS  exitosamente "),
    SESSION_SUCCES("Inicio de sesion exitoso"),
    TOKEN_INVALIDO("El token JWT es invalido!");

    private final String message;
}
