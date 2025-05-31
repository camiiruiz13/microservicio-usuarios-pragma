package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.impl;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserRequestDTOMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final UserRequestDTOMapper userRequestDTOMapper;

    @Override
    public void crearUserPropietario(UserDTO userDTO, String role) {
        User user = userRequestDTOMapper.toUser(userDTO);
        userServicePort.crearUserPropietario(user, role);
    }
}
