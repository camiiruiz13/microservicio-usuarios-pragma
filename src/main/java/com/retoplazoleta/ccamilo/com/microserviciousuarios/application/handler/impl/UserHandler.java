package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.impl;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserRequestDTOMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserResponseDTOMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {


    private final IUserServicePort userServicePort;
    private final UserRequestDTOMapper userRequestDTOMapper;
    private final UserResponseDTOMapper userResponseDTOMapper;

    @Override
    public void createUser(UserDTO userDTO, String role) {
        User user = userRequestDTOMapper.toUser(userDTO);
        userServicePort.createUser(user, role);
    }

    @Override
    public void createUser(UserDTO userDTO) {
        createUser(userDTO,null);
    }

    @Override
    public UserDTOResponse  login(LoginDTO loginDTO) {
        User user =  userServicePort.login(loginDTO.getCorreo(), loginDTO.getClave());
        return userResponseDTOMapper.toDto(user);

    }

    @Override
    public UserDTOResponse findByCorreo(String correo) {
        User user = userServicePort.findByCorreo(correo);
        return userResponseDTOMapper.toDto(user);
    }

    @Override
    public UserDTOResponse findById(Long id) {
        User user = userServicePort.findById(id);
        return userResponseDTOMapper.toDto(user);
    }

    @Override
    public List<UserDTOResponse> fetchEmployeesAndClients(List<Long> idChefs, List<Long> idClients) {
        return userResponseDTOMapper.toDtoList(userServicePort.fetchEmployeesAndClients(idChefs, idClients));
    }
}
