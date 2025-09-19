package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.UsuariosDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UsuariosService {
    //OBTENER TODOS LOS  DATOS
    List<UsuariosDTO> findAllUsuarios();

    //OBTNER UN USUARIOS POR ID
    UsuariosDTO getUsuariosById(Long idUsuarios);

    //CREAR UN NUEVO USUARIO
    UsuariosDTO crearUsuario(UsuariosDTO usuariosDTO);

    //ACTUALIZAR UN USUARIO EXISTENTE
    UsuariosDTO updateUsuarios(Long idUsuarios, UsuariosDTO usuariosDTO);

    //ELIMINAR USUARIOS POR ID
    void deleteUsuarios(Long idUsuarios);

    List<UsuariosDTO> findDuenosByVeterinarioId(Long idVeterinario);
}
