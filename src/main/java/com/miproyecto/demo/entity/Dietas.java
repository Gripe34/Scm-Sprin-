package com.miproyecto.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dietas")
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
    private Mascotas mascota;

    @Column(name = "Descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "TipoDieta", nullable = false, length = 100)
    private String tipoDieta;

    @Column(name = "Dieta", nullable = false, length = 255)
    private String dieta;

    // --- Getters y Setters ---
    public Long getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(Long idDieta) {
        this.idDieta = idDieta;
    }

    public Veterinarios getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinarios veterinario) {
        this.veterinario = veterinario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoDieta() {
        return tipoDieta;
    }

    public void setTipoDieta(String tipoDieta) {
        this.tipoDieta = tipoDieta;
    }

    public String getDieta() {
        return dieta;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }
}
