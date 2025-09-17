package com.miproyecto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
