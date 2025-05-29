package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private Long id;

    private String nombre;
    private String descripcion;
}
