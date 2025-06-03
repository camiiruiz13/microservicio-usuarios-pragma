package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.application.util.SwaggerConstants.*;

/**
 * DTO for {@link com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {

    @Schema(description = NOMBRE_DESC, example = NOMBRE_EXAMPLE, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = APELLIDO_DESC, example = APELLIDO_EXAMPLE, requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @Schema(description = NUMERO_DOC_DESC, example = NUMERO_DOC_EXAMPLE, requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroDocumento;

    @Schema(description = CELULAR_DESC, example = CELULAR_EXAMPLE, requiredMode = Schema.RequiredMode.REQUIRED)
    private String celular;

    @Schema(description = FECHA_NAC_DESC, example = FECHA_NAC_EXAMPLE, pattern = FECHA_NAC_PATTERN, requiredMode = Schema.RequiredMode.REQUIRED)
    private String fechaNacimiento;

    @Schema(description = CORREO_DESC, example = CORREO_EXAMPLE, requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @Schema(description = CLAVE_DESC, example = CLAVE_EXAMPLE, minLength = CLAVE_MIN, maxLength = CLAVE_MAX, requiredMode = Schema.RequiredMode.REQUIRED)
    private String clave;
}