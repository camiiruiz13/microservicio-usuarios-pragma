package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.adapter;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception.NoDataFoundException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.CORREO_NO_EXIST;


@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;


    @Override
    public User getUsuarioByCorreo(String correo) {
        return userRepository.findByCorreo(correo)
                .map(userEntityMapper::toUserModel)
                .orElseThrow(()-> new NoDataFoundException(CORREO_NO_EXIST.getMessage()));
    }
}
