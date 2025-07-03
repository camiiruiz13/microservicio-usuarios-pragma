package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.RoleCode;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;

import java.util.List;

public interface IUserPersistencePort {

    User saveUser(User user);
    User getUsuarioByCorreo(String correo);
    User getUsuarioById(Long id);
    Role getRoleByNombre(String nombre);
    User getUsuarioByNumeroDocumento(String numeroDocumento);
    List<User> fetchEmployeesAndClients(List<Long> userIds, List<RoleCode> roles);

}
