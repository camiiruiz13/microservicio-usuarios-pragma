package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.config;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IPasswordPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.usecase.UserUseCase;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.encoder.PasswordAdapter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {


    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Bean
    IUserPersistencePort userPersistencePort(){
     return new UserJpaAdapter(roleRepository, userRepository, userEntityMapper);
    }

    @Bean
    IPasswordPersistencePort passwordPersistencePort(){
        return new PasswordAdapter(passwordEncoder);
    }

    @Bean
    IUserServicePort userServicePort(){
        return new UserUseCase(passwordPersistencePort(),userPersistencePort());
    }


}
