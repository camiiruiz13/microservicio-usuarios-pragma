package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;

public interface IUserPersistencePort {

    User getUsuarioByCorreo(String correo);
}
