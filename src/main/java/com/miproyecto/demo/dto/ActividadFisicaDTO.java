package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadFisicaDTO {

    private Long idActividadF;
    private Long idVeterinario;
    private String descripcion;
    private String tipoActividad;

    /**
     * Este campo se usará para guardar el NOMBRE del archivo en la base de datos.
     */
    private String foto;

    // Campos para las relaciones y vistas
    private Long idMascota;
    private String nombreMascota;
    private String nombreVeterinario;

    /**
     * ---- CAMPO AÑADIDO ----
     * Este campo recibirá el archivo de imagen subido desde el formulario HTML.
     * No se guarda en la base de datos, se procesa en el servicio.
     */
    private MultipartFile archivoFoto;
}