package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.impl;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.TokenInvalidException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper.UserRequestDTOMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.UserMessagesException.TOKEN_INVALID;

@Service
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {

    private final AuthenticationManager authenticationManager;
    private final IUserServicePort userServicePort;
    private final UserRequestDTOMapper userRequestDTOMapper;

    @Override
    public void crearUserPropietario(UserDTO userDTO, String role) {
        User user = userRequestDTOMapper.toUser(userDTO);
        userServicePort.crearUserPropietario(user, role);
    }

    @Override
    public GenericResponseDTO login(LoginDTO loginDTO) {
        AuthenticationFilter jwtAuthenticationFilter = new AuthenticationFilter(authenticationManager);
        UserDTO userDTO = new UserDTO();
        userDTO.setCorreo(loginDTO.getCorreo());
        userDTO.setClave(loginDTO.getClave());
        User user = userRequestDTOMapper.toUser(userDTO);
        Authentication authentication = jwtAuthenticationFilter.authenticateUser(user);
        try {
            return jwtAuthenticationFilter.generateTokenResponse(authentication);
        } catch (IOException e) {
            throw new TokenInvalidException(TOKEN_INVALID.getMessage() ,e);
        }

    }
}
