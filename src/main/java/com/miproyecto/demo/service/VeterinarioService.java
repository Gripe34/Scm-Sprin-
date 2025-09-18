package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.VeterinariosDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface VeterinarioService {

   //OBTENER DATOS
    List<VeterinariosDTO> findAllVeterinarios();

    //OBTENER UN VETERINARIO POR ID
    VeterinariosDTO getVeterionarioById(Long idVeterinarios);

    //CREAR UN NUEVO VETERINARIO
    VeterinariosDTO crearVeterinario(VeterinariosDTO veterinariosDTO);

    //ACTUALIZAR UN VETERINARIO EXISTENTE
    VeterinariosDTO updateVeterinario(Long idVeterinario, VeterinariosDTO veterinariosDTO);

    //ELIMINAR VETERINARIOS POR ID
    void deleteVeterinario(Long idVeterinario);


}
