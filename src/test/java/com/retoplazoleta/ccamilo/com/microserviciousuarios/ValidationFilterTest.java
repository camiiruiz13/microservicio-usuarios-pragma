package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth.ValidationFilter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Date;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig.PREFIX_TOKEN;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ValidationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;


    @InjectMocks
    ValidationFilter validationFilter;

    @Test
    @Order(1)
    void doFilterInternal_sinAuthorizationHeader_continuaCadena() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        TestUtil.invokePrivateMethod(
                validationFilter,
                "doFilterInternal",
                void.class,
                new Class[]{HttpServletRequest.class, HttpServletResponse.class, FilterChain.class},
                request, response, chain
        );

        verify(chain).doFilter(request, response);
    }

    @Test
    @Order(2)
    void doFilterInternal_tokenValido_autenticaYContinua() throws Exception {
        String correo = "usuario@test.com";

        String token = Jwts.builder()
                .subject(correo)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();

        when(request.getHeader(HEADER_AUTHORIZATION)).thenReturn(PREFIX_TOKEN  + token);

        User user = new User();
        user.setId(1L);
        Role rol = new Role();
        rol.setNombre("ADMIN");
        user.setRol(rol);
        user.setCorreo(correo);

        when(userPersistencePort.getUsuarioByCorreo(correo)).thenReturn(user);



        TestUtil.invokePrivateMethod(
                validationFilter,
                "doFilterInternal",
                void.class,
                new Class[]{HttpServletRequest.class, HttpServletResponse.class, FilterChain.class},
                request, response, chain
        );

        verify(chain).doFilter(request, response);
    }

    @Test
    @Order(3)
    void doFilterInternal_tokenInvalido_enviaRespuestaError() throws Exception {
        String tokenInvalido = "Bearer token-invalido";

        when(request.getHeader("Authorization")).thenReturn(tokenInvalido);

        try (MockedStatic<ResponseUtils> utilsMock = mockStatic(ResponseUtils.class)) {
            utilsMock.when(() -> ResponseUtils.write(any(), any(), eq(401)))
                    .thenAnswer(inv -> null);

            TestUtil.invokePrivateMethod(
                    validationFilter,
                    "doFilterInternal",
                    void.class,
                    new Class[]{HttpServletRequest.class, HttpServletResponse.class, FilterChain.class},
                    request, response, chain
            );

            utilsMock.verify(() -> ResponseUtils.write(
                    eq(response),
                    any(),
                    eq(401)
            ));
        }
    }

}
