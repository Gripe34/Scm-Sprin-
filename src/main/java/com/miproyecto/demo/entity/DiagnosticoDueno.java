package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "diagnostico_dueno")

@Getter // Lombok genera TODOS los getters
@Setter
public class DiagnosticoDueno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostico_dueno")
    private Long idDiagnosticoDueno;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascotas mascota;

    @ManyToOne
    @JoinColumn(name = "id_veterinarios", nullable = false)
    private Veterinarios veterinario;


    @Column(name = "fecha_diagnostico", nullable = false)
    private LocalDate fechaDiagnostico;

    @Column(name = "observaciones", nullable = false, length = 200)
    private String observaciones;

    // Getters y Setters

    public void setIdDiagnostico(Long idDiagnostico) {
        this.idDiagnosticoDueno = idDiagnostico;
    }


    public void setVeterinarios(Veterinarios veterinarios) {
        this.veterinario = veterinarios;
    }

    public void setMascotas(Mascotas mascotas) {
        this.mascota = mascotas;
    }



    public void setFechaDiagnostico(LocalDate fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
