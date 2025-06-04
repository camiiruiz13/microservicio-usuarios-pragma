package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByNombre(String name);



}