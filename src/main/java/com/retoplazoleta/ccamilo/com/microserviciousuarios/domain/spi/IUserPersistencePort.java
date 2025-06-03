package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;

public interface IUserPersistencePort {

    User saveUser(User user, String rol);
    User saveUserClient(User user);
    User getUsuarioByCorreo(String correo);
    User getUsuarioByNumeroDocumento(String numeroDocumento);
}
