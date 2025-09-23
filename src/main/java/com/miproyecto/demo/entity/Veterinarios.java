package com.miproyecto.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "veterinarios")
public class Veterinarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinarios")
    private Long idVeterinario;

    @OneToMany(mappedBy = "veterinario")
    private List<Citas> citas; // relación con la tabla citas





    @Column(name = "Especialidad", nullable = false, length = 200)
    private String especialidad;

    @OneToOne
    @JoinColumn(name = "id_usuarios", nullable = false, unique = true)
    private Usuarios usuario; // relación directa con la tabla usuarios

    // Getters
    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public List<Citas> getCitas() {
        return citas;
    }



    public String getEspecialidad() {
        return especialidad;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    // Setters
    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public void setCitas(List<Citas> citas) {
        this.citas = citas;
    }


    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public void setNombre(String nombre) {

    }
}
