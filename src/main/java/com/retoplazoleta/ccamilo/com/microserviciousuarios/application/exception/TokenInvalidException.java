package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception;

public class TokenInvalidException extends RuntimeException{

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenInvalidException(String message) {
        super(message);
    }
}
