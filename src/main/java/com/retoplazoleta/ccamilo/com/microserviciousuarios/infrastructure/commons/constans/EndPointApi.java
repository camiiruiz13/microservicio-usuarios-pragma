package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

public interface EndPointApi {

    String BASE_URL = "/api/users";
    String CREATE_USER = "/createUser";
    String LOGIN = "/login";
    String FIND_BY_CORREO = "/buscarPorCorreo/{correo}";
    String CREATE_USER_CLIENT = "/createUserClient";

}
