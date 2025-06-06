package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.mock.web.DelegatingServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.ERROR_CREDENCIALES;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.SESSION_SUCCES;
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
        LoginDTO user = new LoginDTO();
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
    void generateTokenResponse_debeRetornarTokenYUsername()  {
        Collection<? extends GrantedAuthority> authorities = List.of(() -> "ROLE_USER");

        AuthenticatedUser authenticatedUser = new AuthenticatedUser("1", "user@example.com", "user@example.com", authorities);

        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        Map<String, Object> result = authenticationFilter.generateTokenResponse(authentication);

        assertEquals("user@example.com", result.get("username"));
        assertNotNull(result.get("token"));
        assertTrue(((String) result.get("token")).contains(".")); // verifica que tiene formato JWT
    }


    @Test
    @Order(3)
    void attemptAuthentication_debeLeerYAutenticarUsuario() throws Exception {

        LoginDTO user = new LoginDTO();
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
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("1", "test@correo.com", "test@correo.com", authorities);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        Map<String, Object> mockMap = Map.of(
                "token", "mock-token",
                "username", "test@correo.com"
        );

        GenericResponseDTO<Map<String, Object>> dtoEsperado = new GenericResponseDTO<>();
        dtoEsperado.setObjectResponse(mockMap); // o setData() según tu clase
        dtoEsperado.setMessage(SESSION_SUCCES.getMessage() + "test@correo.com");

        AuthenticationFilter spyFilter = Mockito.mock(AuthenticationFilter.class, withSettings()
                .useConstructor(authenticationManager)
                .defaultAnswer(CALLS_REAL_METHODS));

        when(spyFilter.generateTokenResponse(authentication)).thenReturn(mockMap);

        try (MockedStatic<ResponseUtils> utilsMock = mockStatic(ResponseUtils.class)) {
            utilsMock.when(() -> ResponseUtils.buildResponse(
                    eq(SESSION_SUCCES.getMessage() + "test@correo.com"),
                    eq(mockMap),
                    eq(HttpStatus.OK)
            )).thenReturn(dtoEsperado);

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
                    eq(dtoEsperado),
                    eq(200),
                    eq("Bearer mock-token")
            ));
        }
    }





}
