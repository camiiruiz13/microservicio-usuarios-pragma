package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth.AuthenticationFilter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.mock.web.DelegatingServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.ERROR_CREDENCIALES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @Order(1)
    void authenticateUser_debeAutenticarCorrectamente() {
        User user = new User();
        user.setCorreo("test@correo.com");
        user.setClave("clave123");

        UsernamePasswordAuthenticationToken expectedToken =
                new UsernamePasswordAuthenticationToken("test@correo.com", "clave123");

        when(authenticationManager.authenticate(expectedToken)).thenReturn(authentication);

        Authentication result = authenticationFilter.authenticateUser(user);

        assertEquals(authentication, result);
        verify(authenticationManager).authenticate(expectedToken);
    }

    @Test
    @Order(2)
    void generateTokenResponse_debeRetornarGenericResponseDTO() throws Exception {

        Collection<? extends GrantedAuthority> authorities = List.of(() -> "ROLE_USER");

        UserDetails springUser = new org.springframework.security.core.userdetails.User(
                "user@example.com", "password", authorities
        );

        when(authentication.getPrincipal()).thenReturn(springUser);
        doReturn(authorities).when(authentication).getAuthorities();

        GenericResponseDTO<?> dto = authenticationFilter.generateTokenResponse(authentication);


        assertEquals(200, dto.getStatusCode());

        Map<?, ?> body = (Map<?, ?>) dto.getObjectResponse();
        assertEquals("user@example.com", body.get("username"));
        assertNotNull(body.get("token"));
        assertTrue(((String) body.get("token")).contains("."));
    }

    @Test
    @Order(3)
    void attemptAuthentication_debeLeerYAutenticarUsuario() throws Exception {

        User user = new User();
        user.setCorreo("correo@correo.com");
        user.setClave("123");

        String json = objectMapper.writeValueAsString(user);

        when(request.getInputStream()).thenReturn(
                new DelegatingServletInputStream(
                        new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))
        );
        when(authenticationManager.authenticate(any())).thenReturn(authentication);


        Authentication result = authenticationFilter.attemptAuthentication(request, response);


        assertEquals(authentication, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }


    @Test
    @Order(4)
    void attemptAuthentication_lanzaExcepcionSiErrorLectura() throws Exception {

        when(request.getInputStream()).thenThrow(new IOException("Fallo en lectura"));


        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authenticationFilter.attemptAuthentication(request, response));

        assertTrue(ex.getMessage().contains(ERROR_CREDENCIALES.getMessage()));
    }

    @Test
    @Order(5)
    void successfulAuthentication_escribeTokenEnRespuesta() throws Exception {
        Collection<? extends GrantedAuthority> authorities = List.of(() -> "ROLE_USER");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@correo.com", "clave", authorities
        );


        GenericResponseDTO<?> mockResponse = GenericResponseDTO.builder()
                .statusCode(200)
                .message("OK")
                .objectResponse(Map.of("token", "mock-token", "username", "test@correo.com"))
                .build();

        AuthenticationFilter spyFilter = spy(authenticationFilter);

        doReturn(mockResponse).when(spyFilter)
                .generateTokenResponse(authentication);

        try (MockedStatic<ResponseUtils> utilsMock = mockStatic(ResponseUtils.class)) {
            utilsMock.when(() -> ResponseUtils.write(any(), any(), anyInt(), anyString()))
                    .thenAnswer(invocation -> null);

            TestUtil.invokePrivateMethod(
                    spyFilter,
                    "successfulAuthentication",
                    void.class,
                    new Class[]{HttpServletRequest.class, HttpServletResponse.class, FilterChain.class, Authentication.class},
                    request, response, filterChain, authentication
            );

            utilsMock.verify(() -> ResponseUtils.write(
                    eq(response),
                    any(GenericResponseDTO.class),
                    eq(200),
                    startsWith("Bearer ")
            ));
        }
    }

}
