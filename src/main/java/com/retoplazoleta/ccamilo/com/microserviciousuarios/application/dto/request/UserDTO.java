package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {
    private Long idUser;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private String celular;
    private String fechaNacimiento;
    private String correo;
    private String clave;
}