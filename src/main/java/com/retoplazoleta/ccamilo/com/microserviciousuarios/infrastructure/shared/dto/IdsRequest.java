package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto;


import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IdsRequest extends GenericRequest<UserFilterDTO>{

    public IdsRequest(UserFilterDTO request) {
        super(request);
    }
}
