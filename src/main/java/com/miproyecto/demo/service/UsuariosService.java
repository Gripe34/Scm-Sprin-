package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.UsuariosDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UsuariosService {
    //OBTENER TODOS LOS  DATOS
    List<UsuariosDTO> findAllUsuarios();

    Page<UsuariosDTO> obtenerUsuariosConPaginacion(Pageable pageable);

    String restablecerContrasena(Long idUsuario);

    //OBTNER UN USUARIOS POR ID
    UsuariosDTO getUsuariosById(Long idUsuarios);

    //CREAR UN NUEVO USUARIO
    UsuariosDTO crearUsuario(UsuariosDTO usuariosDTO);

    //ACTUALIZAR UN USUARIO EXISTENTE
    UsuariosDTO updateUsuarios(Long idUsuarios, UsuariosDTO usuariosDTO);

    //ELIMINAR USUARIOS POR ID
    void deleteUsuarios(Long idUsuarios);

    List<UsuariosDTO> findDuenosByVeterinarioId(Long idVeterinario);

    void bloquearUsuario(Long idUsuario);
    List<UsuariosDTO> findDuenosByVeterinarioId(Long idVeterinario, String nombre, String apellido, String correo);
}
