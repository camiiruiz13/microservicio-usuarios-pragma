package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.adapter;


import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.RoleCode;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.entities.UserEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = userEntityMapper.toUserEntity(user);
        userEntity = userRepository.save(userEntity);
        return userEntityMapper.toUserModel(userEntity);
    }

    @Override
    public User getUsuarioByCorreo(String correo) {
        return userRepository.findByCorreo(correo)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }

    @Override
    public User getUsuarioById(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }

    @Override
    public Role getRoleByNombre(String nombre) {
        return roleRepository.findByNombre(nombre)
                .map(userEntityMapper::toRoleModel)
                .orElse(null);
    }

    @Override
    public User getUsuarioByNumeroDocumento(String numeroDocumento) {
        return userRepository.findByNumeroDocumento(numeroDocumento)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }

    @Override
    public List<User> fetchEmployeesAndClients(List<Long> userIds, List<String> roles ) {
        return userEntityMapper.toUserModelList(userRepository.fetchEmployeesAndClients(userIds, roles));
    }

}

