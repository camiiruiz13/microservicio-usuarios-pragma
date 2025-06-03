package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;


public interface IUserHandler {
    void crearUserPropietario(UserDTO userDTO, String role);
}
