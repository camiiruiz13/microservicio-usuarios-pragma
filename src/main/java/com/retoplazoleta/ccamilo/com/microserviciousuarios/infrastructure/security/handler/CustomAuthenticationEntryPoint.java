package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.handler;


import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.INVALID_TOKEN;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String ERROR = "error";
    private final String CODIGO = "codigo";
    private final String MENSAJE = "mensaje";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Map<String, Object> error = new HashMap<>();
        error.put(MENSAJE, INVALID_TOKEN);
        error.put(CODIGO , HttpStatus.FORBIDDEN.value());
        error.put(ERROR, authException.getMessage());


        ResponseUtils.write(response, error, HttpStatus.FORBIDDEN.value());
    }
}
