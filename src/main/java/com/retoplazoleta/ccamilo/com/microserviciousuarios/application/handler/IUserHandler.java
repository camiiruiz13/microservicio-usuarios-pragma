package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;

public interface IUserHandler {
    void createUser(UserDTO userDTO, String role);
    void createUser(UserDTO userDTO);
    UserDTOResponse login(LoginDTO loginDTO);
    UserDTOResponse findByCorreo(String correo);
}
