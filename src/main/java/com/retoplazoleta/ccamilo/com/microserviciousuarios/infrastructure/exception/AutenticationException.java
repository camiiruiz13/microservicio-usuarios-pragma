package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception;

public class AutenticationException extends RuntimeException{

    public AutenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutenticationException(String message) {
        super(message);
    }
}
