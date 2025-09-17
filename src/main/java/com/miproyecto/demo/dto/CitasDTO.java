package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitasDTO {

    private Long idCita;

    private Long idMascota;

    private Long idVeterinario;

    private LocalDateTime fechaCita;

    private String motivoCita;

    private String estadoCita;

    private Long idDiagnostico;

    public Long getIdCita() {
        return idCita;
    }
}
