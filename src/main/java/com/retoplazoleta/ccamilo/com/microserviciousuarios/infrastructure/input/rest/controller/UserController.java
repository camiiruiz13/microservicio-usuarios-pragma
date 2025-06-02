package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.controller;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.handler.IUserHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.dto.GenericRequest;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.EndPointApi.BASE_URL;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.EndPointApi.CREATE_USER;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.CREATE_USER_SUCCES;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class UserController {

    private final IUserHandler userHandler;
    private final ModelMapper modelMapper;

    @PostMapping(CREATE_USER)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO<Void>> createUser(@RequestBody GenericRequest request, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        UserDTO userDTO = modelMapper.map(request, UserDTO.class);
        userHandler.crearUserPropietario(userDTO, authenticatedUser.getRol());
        return new ResponseEntity<>(ResponseUtils.buildResponse(CREATE_USER_SUCCES.getMessage(), HttpStatus.CREATED), HttpStatus.CREATED);

    }

}
