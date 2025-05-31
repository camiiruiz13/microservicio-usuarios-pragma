package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO implements Serializable {
   private Long idRol;
   private String nombre;
   private String descripcion;
}