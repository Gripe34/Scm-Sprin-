package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosDTO {

    private Long idUsuario;

    private String nombre;

    private String apellido;

    private String correo;

    private String contrasena;

    private Long telefono;

    private String direccion;

    private Long idRol;

    private String foto;

    private Boolean habilitado;
    private List<MascotasDTO> mascotas;
}