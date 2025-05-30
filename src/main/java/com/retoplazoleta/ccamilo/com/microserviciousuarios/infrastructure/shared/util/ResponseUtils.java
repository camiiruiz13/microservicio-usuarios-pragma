package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.dto.GenericResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig.CONTENT_TYPE;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig.HEADER_AUTHORIZATION;


public class ResponseUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private ResponseUtils() {
    }

    public static <T> GenericResponseDTO<T> buildResponse(String message, T objectResponse, HttpStatus status) {
        return GenericResponseDTO.<T>builder()
                .message(message)
                .objectResponse(objectResponse)
                .statusCode(status.value())
                .build();
    }

    public static void write(HttpServletResponse response, Object body, int status) throws IOException {
        write(response, body, status, null);
    }

    public static void write(HttpServletResponse response, Object body, int status, String token) throws IOException {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE);
        if (token != null) {
            response.addHeader(HEADER_AUTHORIZATION, token);
        }
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
