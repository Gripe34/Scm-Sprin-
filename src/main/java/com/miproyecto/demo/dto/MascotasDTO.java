package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MascotasDTO {

    private Long idMascota;

    private String foto;

    private  String nombre;

    private String genero;

    private LocalDate fechaNacimiento;

    private String raza;

    private Long idUsuario;
    private MultipartFile archivoFoto;
    private String nombreDueno;
}
