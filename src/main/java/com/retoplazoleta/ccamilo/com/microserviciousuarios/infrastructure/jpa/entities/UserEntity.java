package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_usuario_correo", columnNames = "correo"),
                @UniqueConstraint(name = "uq_usuario_documento", columnNames = "numero_documento")
        }
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "numero_documento", nullable = false)
    private String numeroDocumento;

    @Column(name = "celular")
    private String celular;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(name = "clave")
    private String clave;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_rol",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_rol")
    )
    private RoleEntity rol;
}
