package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;

public interface IUserHandler {
    void crearUserPropietario(UserDTO userDTO, String role);
    GenericResponseDTO login(LoginDTO loginDTO);
}
