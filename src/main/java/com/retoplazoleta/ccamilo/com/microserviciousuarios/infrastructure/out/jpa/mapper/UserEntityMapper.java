package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.mapper;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.entities.RoleEntity;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.entities.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserEntityMapper {
    UserEntity toUserEntity(User user);

    User toUserModel(UserEntity userEntity);

    RoleEntity toRoleEntity(Role model);

    Role toRoleModel(RoleEntity entity);

    List<User> toUserModelList(List<UserEntity> userEntities);


}