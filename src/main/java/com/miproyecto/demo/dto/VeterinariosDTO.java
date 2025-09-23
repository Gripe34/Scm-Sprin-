package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeterinariosDTO {

    private Long idVeterinario;

private String nombre;

    private String apellido;

    private String especialidad;

    private Long telefono;

    private Long idUsuario;

    private String correo;

    private String contrasena;

    private String direccion;

    private Boolean habilitado;




}
