package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietasDTO {

    private Long idDieta;
    private Long idMascota;
    private Long idVeterinario;
    private String descripcion;
    private String tipoDieta;
    private String dieta;

}
