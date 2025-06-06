package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for {@link UserEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private String celular;
    private LocalDate fechaNacimiento;
    private String correo;
    private String clave;
    private Role rol;

}
