package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.adapter;


import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.RoleCode;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.RoleEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.UserEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;




@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final  PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;


    @Override
    public User saveUser(User user, String rol) {
        UserEntity userEntity = userEntityMapper.toUserEntity(user);
        RoleEntity role = new RoleEntity();
        if (rol == null || rol.trim().isEmpty()) {
            role = roleRepository.findByNombre(RoleCode.CLIENTE.name());
        } else if (rol.equals(RoleCode.ADMIN.name())) {
            role = roleRepository.findByNombre(RoleCode.PROPIETARIO.name());
        } else if (rol.equals(RoleCode.PROPIETARIO.name())) {
            role = roleRepository.findByNombre(RoleCode.EMPLEADO.name());
        }


        userEntity.setRol(role);
        userEntity.setClave(passwordEncoder.encode(userEntity.getClave()));
        userEntity = userRepository.save(userEntity);

        return userEntityMapper.toUserModel(userEntity);
    }

    @Override
    public User saveUserClient(User user) {
       return saveUser(user, null);
    }

    @Override
    public User getUsuarioByCorreo(String correo) {
        return userRepository.findByCorreo(correo)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }

    @Override
    public User getUsuarioByNumeroDocumento(String numeroDocumento) {
        return userRepository.findByNumeroDocumento(numeroDocumento)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }
}
