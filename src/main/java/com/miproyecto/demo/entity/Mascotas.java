package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@Entity
@Table(name = "mascotas")
public class Mascotas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota", nullable = false, unique = true)
    private Long idMascota;
    @Column(name = "foto", nullable = false)
    private String foto;
    @Column(name = "nombre", nullable = false)
    private  String nombre;
    @Column(name = "genero", nullable = false)
    private String genero;
    @Column(name = "fechaNacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    @Column(name = "raza", nullable = false)
    private String raza;
    //Relacion IdUsuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;



}
