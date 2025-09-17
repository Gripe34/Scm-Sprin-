package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MascotasDTO {

    private Long idMascota;
    private Long idActividadFisica;
    private Long idDieta;
    private Long idCita;
    private Long idUsuario;
    private String nombre;
    private String genero;
    private Long fechaNacimiento; // mejor usar LocalDate
    private String raza;
    private String foto;
}
