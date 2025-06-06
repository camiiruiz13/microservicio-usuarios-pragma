package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.encoder.UserDetailsAdapter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDetailsAdapterTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private UserDetailsAdapter userDetailsAdapter;

    @Test
    @Order(1)
    void loadUserByUsername_debeRetornarAuthenticatedUserCorrectamente() {
        String username = "test@correo.com";
        Long userId = 1L;

        Role rol = new Role();
        rol.setNombre("ADMIN");

        User user = new User();
        user.setId(userId);
        user.setCorreo(username);
        user.setRol(rol);
        user.setClave("clave123");

        when(userPersistencePort.getUsuarioByCorreo(username)).thenReturn(user);

        UserDetails userDetails = userDetailsAdapter.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertInstanceOf(AuthenticatedUser.class, userDetails);

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetails;

        assertEquals(userId.toString(), authenticatedUser.getIdUser());
        assertEquals(username, authenticatedUser.getUsername());
        assertEquals("clave123", authenticatedUser.getPassword()); // ← CORRECCIÓN

        assertEquals(1, authenticatedUser.getAuthorities().size());
        assertTrue(authenticatedUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        verify(userPersistencePort).getUsuarioByCorreo(username);
    }

}
