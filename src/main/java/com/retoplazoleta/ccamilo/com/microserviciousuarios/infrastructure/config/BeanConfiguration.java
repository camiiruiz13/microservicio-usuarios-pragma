package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.config;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.usecase.UserUseCase;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.adapter.UserJpaAdapter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.mapper.UserEntityMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.RoleRepository;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
     return new UserJpaAdapter(passwordEncoder,roleRepository, userRepository, userEntityMapper);
    }

    @Bean
    IUserServicePort userServicePort(){
        return new UserUseCase(userPersistencePort());
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
