package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import lombok.Getter; // Importante
import lombok.Setter; // Importante

@Entity
@Table(name = "dietas")
@Getter // Lombok genera TODOS los getters
@Setter // Lombok genera TODOS los setters
public class Dietas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dieta")
    private Long idDieta;

    @ManyToOne
    @JoinColumn(name = "IdVeterinario", nullable = false)
    private Veterinarios veterinario;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascotas mascota; // <-- El campo que causaba el error

    @Column(name = "Descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "TipoDieta", nullable = false, length = 100)
    private String tipoDieta;

    @Column(name = "Dieta", nullable = false, length = 255)
    private String dieta;

    // --- ¡YA NO NECESITAS ESCRIBIR NINGÚN GETTER O SETTER AQUÍ! ---
}