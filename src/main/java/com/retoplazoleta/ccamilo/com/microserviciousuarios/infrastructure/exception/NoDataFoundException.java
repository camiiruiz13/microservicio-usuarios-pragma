package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String mensaje) {
        super(mensaje);
    }
}
