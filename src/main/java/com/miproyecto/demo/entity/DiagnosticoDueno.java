package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "diagnostico_dueno")
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

    // Si Medico es entidad: @ManyToOne
    @Column(name = "id_medico")
    private Long idMedico;

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

    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
    }

    public void setFechaDiagnostico(LocalDate fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
