package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "actividad_fisica")
@Data
public class ActividadFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividadf")
    private Long idActividadF;

    @ManyToOne
    @JoinColumn(name = "id_veterinario", nullable = false)
    private Veterinarios veterinario;  // relación directa a la entidad

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "tipo_actividad", nullable = false, length = 100)
    private String tipoActividad;

    @Column(name = "foto", nullable = false, length = 255)
    private String foto;
}
