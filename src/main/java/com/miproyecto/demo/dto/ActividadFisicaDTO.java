package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ActividadFisicaDTO {

    private Long idActividadF;
    private Long idVeterinario;
    private String descripcion;
    private String tipoActividad;
    private String foto;

    // Getters y Setters

    public Long getIdActividadF() {
        return idActividadF;
    }

    public void setIdActividadF(Long idActividadF) {
        this.idActividadF = idActividadF;
    }


    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
