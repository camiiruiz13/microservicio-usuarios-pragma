package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponseDTO<T> {


    private String message;
    private T objectResponse;
    private int statusCode;
}
