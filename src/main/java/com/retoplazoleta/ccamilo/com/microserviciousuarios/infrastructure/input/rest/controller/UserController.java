package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.EndPointApi.*;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.CREATE_USER_SUCCES;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.FIND_USER_SUCCES;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.SwaggerConstants.*;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig.CONTENT_TYPE;

@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class UserController {

    private final IUserHandler userHandler;


    @Operation(summary = CREATE_USER_SUMMARY, description = CREATE_USER_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_201, description = RESPONSE_201,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_400, description = RESPONSE_400,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_409, description = RESPONSE_409,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_500, description = RESPONSE_500,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class)))
    })
    @PostMapping(CREATE_USER)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO<Void>> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = CREATE_USER_DESCRIPTION_REQUEST,
                    content = @Content(
                            mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = UserRequest.class)
                    )
            )
            @RequestBody UserRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        UserDTO userDTO =request.getRequest();
        userHandler.crearUserPropietario(userDTO, authenticatedUser.getRol());
        return new ResponseEntity<>(
                ResponseUtils.buildResponse(CREATE_USER_SUCCES.getMessage(), HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = FIND_USER_SUMMARY, description = FIND_USER_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200, description = RESPONSE_200,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_400, description = RESPONSE_400,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_409, description = RESPONSE_409,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = HTTP_500, description = RESPONSE_500,
                    content = @Content(schema = @Schema(implementation = GenericResponseDTO.class)))
    })
    @GetMapping(FIND_BY_CORREO)
    public ResponseEntity<GenericResponseDTO<UserDTOResponse>> findByCorreo(
            @PathVariable("correo")
            @Parameter(description = FIND_BY_CORREO, required = true)
            String correo){

        UserDTOResponse userDTO = userHandler.findByCorreo(correo);

        return new ResponseEntity<>(
                ResponseUtils.buildResponse(FIND_USER_SUCCES.getMessage(), userDTO, HttpStatus.OK),
                HttpStatus.OK
        );
    }
}


