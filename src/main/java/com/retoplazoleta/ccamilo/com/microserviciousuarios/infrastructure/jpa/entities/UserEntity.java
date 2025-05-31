package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "numero_documento", unique = true, nullable = true)
    private String numeroDocumento;

    @Column(name = "celular",  nullable = true)
    private String celular;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "correo", unique = true, nullable = true)
    private String correo;

    @Column(name = "clave", nullable = true)
    private String clave;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = true)
    private RoleEntity rol;
}
