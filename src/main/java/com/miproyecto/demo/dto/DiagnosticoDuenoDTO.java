package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticoDuenoDTO {

    private Long idDiagnostico;



    private Long idMascota;

    private Long idVeterinarios;

    private LocalDate fechaDiagnostico;
    private String nombreMascota;
    private String nombreDueno;
    private String observaciones;
}
