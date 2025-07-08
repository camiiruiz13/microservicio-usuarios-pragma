package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans;

public interface EndPointApi {

    String BASE_URL = "/api/users";
    String CREATE_USER = "/createUser";
    String LOGIN = "/login";
    String FIND_BY_CORREO = "/buscarPorCorreo/{correo}";
    String FIND_BY_ID = "/buscarPorId/{id}";
    String FIND_BY_ID_USERS = "/buscarPorIds";
    String CREATE_USER_CLIENT = "/createUserClient";

}
