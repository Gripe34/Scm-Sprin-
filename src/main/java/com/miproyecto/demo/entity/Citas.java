package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")


public class Citas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcita")
    private Long idCita;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascotas mascota;

    @ManyToOne
    @JoinColumn(name = "id_veterinarios", nullable = false)
    private Veterinarios veterinario;

    @OneToOne
    @JoinColumn(name = "id_diagnostico", nullable = false, unique = true)
    private DiagnosticoDueno diagnostico;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "motivo_cita", nullable = false, length = 200)
    private String motivoCita;

    @Column(name = "estado_cita", nullable = false, length = 100)
    private String estadoCita;

    // Getters y Setters

    public Long getIdCita() {
        return idCita;
    }

    public Mascotas getMascota() {
        return mascota;
    }

    public void setMascota(Mascotas mascota) {
        this.mascota = mascota;
    }

    public Veterinarios getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinarios veterinario) {
        this.veterinario = veterinario;
    }

    public DiagnosticoDueno getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(DiagnosticoDueno diagnostico) {
        this.diagnostico = diagnostico;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getMotivoCita() {
        return motivoCita;
    }

    public void setMotivoCita(String motivoCita) {
        this.motivoCita = motivoCita;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }

}
