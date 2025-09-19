package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Esta anotación incluye @Getter, @Setter, @ToString, etc. para todos los campos.
@AllArgsConstructor
@NoArgsConstructor
public class ActividadFisicaDTO {

    private Long idActividadF;
    private Long idVeterinario;
    private String descripcion;
    private String tipoActividad;
    private String foto;

    // Campos integrados correctamente en la clase
    private Long idMascota;
    private String nombreMascota;
    private String nombreVeterinario;
}