package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.RoleDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleResponseMapper {

    @Mapping(source = "id", target = "idRol")
    RoleDTO toDto(Role role);
}
