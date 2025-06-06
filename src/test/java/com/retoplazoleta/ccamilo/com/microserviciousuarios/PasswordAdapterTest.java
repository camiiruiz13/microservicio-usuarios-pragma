package com.retoplazoleta.ccamilo.com.microserviciousuarios;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception.AutenticationException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.encoder.PasswordAdapter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.ERROR_CREDENCIALES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PasswordAdapterTest {


    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordAdapter passwordAdapter;

    @Test
    @Order(2)
    void esClaveValida_claveCorrecta_retornaTrue() {
        String claveIngresada = "12345";
        String claveBD = "$2a$10$abc123";

        when(passwordEncoder.matches(claveIngresada, claveBD)).thenReturn(true);

        boolean resultado = passwordAdapter.esClaveValida(claveIngresada, claveBD);

        assertTrue(resultado);
        verify(passwordEncoder).matches(claveIngresada, claveBD);
    }


    @Test
    @Order(3)
    void esClaveValida_claveIncorrecta_lanzaExcepcion() {
        String claveIngresada = "12345";
        String claveBD = "$2a$10$abc123";

        when(passwordEncoder.matches(claveIngresada, claveBD)).thenReturn(false);

        AutenticationException exception = assertThrows(AutenticationException.class, () ->
                passwordAdapter.esClaveValida(claveIngresada, claveBD));

        assertEquals(ERROR_CREDENCIALES.getMessage(), exception.getMessage());
        verify(passwordEncoder).matches(claveIngresada, claveBD);
    }

}
