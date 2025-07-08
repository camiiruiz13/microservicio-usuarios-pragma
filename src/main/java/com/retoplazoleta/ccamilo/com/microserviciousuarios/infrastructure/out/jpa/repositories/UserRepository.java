package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.repositories;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.RoleCode;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.jpa.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByCorreo(String correo);
    Optional<UserEntity> findByNumeroDocumento(String numeroDocumento);

    @Query(value = "SELECT u FROM UserEntity u " +
            "JOIN FETCH u.rol r " +
            "WHERE u.id IN :userIds " +
            "AND r.nombre IN :roles ")
    List<UserEntity> fetchEmployeesAndClients(@Param("userIds") List<Long> userIds,
                                              @Param("roles")   List<String> roles );
}