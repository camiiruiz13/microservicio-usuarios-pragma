package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.adapter;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDetailsAdapter implements UserDetailsService {

    private final IUserPersistencePort userPersistencePort;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findByCorreo = userPersistencePort.getUsuarioByCorreo(username);

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + findByCorreo.getRol().getNombre())
        );

        return new AuthenticatedUser(findByCorreo.getId().toString(), findByCorreo.getCorreo(), findByCorreo.getCorreo(), authorities);


    }
}
