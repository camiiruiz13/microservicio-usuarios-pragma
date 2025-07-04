package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;

public interface IUserPersistencePort {

    User saveUser(User user);
    User getUsuarioByCorreo(String correo);
    User getUsuarioById(Long id);
    Role getRoleByNombre(String nombre);
    User getUsuarioByNumeroDocumento(String numeroDocumento);

}
