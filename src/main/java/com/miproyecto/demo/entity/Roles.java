package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import lombok.*; // Importaciones específicas

@Entity
@Table(name = "roles")
@Getter // Solo genera getters
@Setter // Solo genera setters
@NoArgsConstructor // Genera el constructor vacío que necesita JPA
@AllArgsConstructor // Genera el constructor con todos los argumentos que necesitabas antes
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "rol", nullable = false, unique = true, length = 200)
    private String rol;
}