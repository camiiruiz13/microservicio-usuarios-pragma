package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;

import java.util.List;

public interface IUserHandler {
    void createUser(UserDTO userDTO, String role);
    void createUser(UserDTO userDTO);
    UserDTOResponse login(LoginDTO loginDTO);
    UserDTOResponse findByCorreo(String correo);
    UserDTOResponse findById(Long id);
    List<UserDTOResponse> fetchEmployeesAndClients(List<Long> idChefs, List<Long> idClients);
}
