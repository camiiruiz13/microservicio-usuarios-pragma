package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;

public interface IUserServicePort {

    void crearUserPropietario(User user, String role);
    User login(String correo, String clave);
    User findByCorreo(String correo);

}
