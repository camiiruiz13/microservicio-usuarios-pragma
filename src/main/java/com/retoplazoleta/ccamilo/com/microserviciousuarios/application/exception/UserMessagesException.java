package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum UserMessagesException {

    DATE_TIME_FORMATER("dd/MM/yyyy"),
    ILEGAL_FROMATER("La fecha debe tener el formato dd/MM/yyyy");

    private final String message;
}
