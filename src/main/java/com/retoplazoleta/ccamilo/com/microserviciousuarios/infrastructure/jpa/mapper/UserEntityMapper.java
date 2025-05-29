package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.mapper;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.RoleEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserEntityMapper {
    UserEntity toUserEntity(User user);

    User toUserModel(UserEntity userEntity);

    RoleEntity toRoleEntity(Role model);

    Role toRoleModel(RoleEntity entity);


}