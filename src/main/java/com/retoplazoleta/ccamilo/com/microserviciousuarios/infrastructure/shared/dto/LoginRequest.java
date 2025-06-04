package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.LoginDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class LoginRequest extends GenericRequest<LoginDTO> {

    public LoginRequest(LoginDTO request) {
        super(request);
    }
}
