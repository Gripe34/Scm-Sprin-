package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.RolesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RolesService {

    //OBTENER TODOS LOS DATOS
    List<RolesDTO> findAllRoles();

    //OBTENER UN ROL POR ID
    RolesDTO getRolesById(Long idRol);

    //CREAR UN NUEVO ROL
    RolesDTO crearRoles(RolesDTO rolesDTO);

    //ACTUALIZAR UN ROL EXISTENTE
    RolesDTO updateRoles(Long idRol, RolesDTO rolesDTO);

    //ELIMINAR ROLES POR ID
    void deleteRoles(Long idRol);
}
