package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;

import java.util.List;

public interface IUserServicePort {

    void createUser(User user, String role);
    void createUser(User user);
    User login(String correo, String clave);
    User findByCorreo(String correo);
    User findById(Long idUser);
    List<User> fetchEmployeesAndClients(List<Long> idChefs, List<Long> idClients);

}
