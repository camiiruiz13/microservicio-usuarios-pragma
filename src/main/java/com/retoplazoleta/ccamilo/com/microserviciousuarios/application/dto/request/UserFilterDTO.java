package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.util.SwaggerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilterDTO {

    @Schema(description = SwaggerConstants.FIND_ID_CLIENTE_DESCRIPTION, example = SwaggerConstants.IDS_ARRAYS, requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> clientIds;

    @Schema(description = SwaggerConstants.FIND_ID_CHEF_DESCRIPTION, example = SwaggerConstants.IDS_ARRAYS, requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> chefIds;
}

