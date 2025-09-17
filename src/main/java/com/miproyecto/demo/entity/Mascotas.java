package com.miproyecto.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "mascotas")
public class Mascotas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Long idMascota;

    @ManyToOne
    @JoinColumn(name = "id_actividadf", nullable = false)
    private ActividadFisica actividadFisica;

    @ManyToOne
    @JoinColumn(name = "id_usuarios", nullable = false)
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "id_dieta", nullable = false)
    private Dietas dieta;

    @OneToMany(mappedBy = "mascota")
    private List<Citas> citas;

    @OneToMany(mappedBy = "mascota")
    private List<DiagnosticoDueno> diagnosticos;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "genero", nullable = false, length = 100)
    private String genero;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "raza", nullable = false, length = 200)
    private String raza;

    @Column(name = "foto", length = 200)
    private String foto;


    // Getters y setters
    public Long getIdMascota() { return idMascota; }
    public void setIdMascota(Long idMascota) { this.idMascota = idMascota; }

    public ActividadFisica getActividadFisica() { return actividadFisica; }
    public void setActividadFisica(ActividadFisica actividadFisica) { this.actividadFisica = actividadFisica; }

    public Usuarios getUsuario() { return usuario; }
    public void setUsuario(Usuarios usuario) { this.usuario = usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento;}

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
