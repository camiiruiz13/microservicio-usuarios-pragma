package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.EndPointApi.*;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.SwaggerConstants.*;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig.CONTENT_TYPE;

@Tag(name = TAG_NAME_LOGIN, description = TAG_DESCRIPTION_LOGIN)
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final IUserHandler userHandler;


    @Operation(summary = LOGIN_SUMMARY, description = LOGIN_DESCRIPTION, security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200, description = RESPONSE_200,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_400, description = RESPONSE_400,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_401, description = RESPONSE_401,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_500, description = RESPONSE_500,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class)))
    })

    @PostMapping(LOGIN)
    public ResponseEntity<GenericResponseDTO<Map<String, Object>>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = CREATE_USER_DESCRIPTION_REQUEST,
                    content = @Content(
                            mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = LoginDTO.class)
                    )
            )
            @RequestBody LoginDTO request) {

        UserDTOResponse userDTOResponse = userHandler.login(request);



    }


}


