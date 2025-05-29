package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.repositories;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByCorreo(String correo);
}