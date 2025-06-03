package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.input.rest.dto;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserRequest extends GenericRequest<UserDTO> {

    public UserRequest(UserDTO request) {
        super(request);
    }
}
