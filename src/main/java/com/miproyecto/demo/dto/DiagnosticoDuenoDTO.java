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

    private Long idCitas;

    private Long idMascota;

    private Long idMedico;

    private Long idVeterinarios;

    private LocalDate fechaDiagnostico;

    private String observaciones;
}
