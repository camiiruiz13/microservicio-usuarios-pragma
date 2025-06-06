package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainConstants {

    REGEX_VALID_EMAIL("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"),
    REGEX_VALID_PHONE("^\\+?\\d{1,13}$");

    private final String message;
}
