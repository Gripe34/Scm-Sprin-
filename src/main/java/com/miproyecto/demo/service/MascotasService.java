package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.MascotasDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MascotasService {

    //OBTENER TODOS LOS DATOS
    List<MascotasDTO> findAllMascotas();

    //OBTENER UNA MASCOTA POR ID
    MascotasDTO getMascotasById(Long idMascota);

    //CREAR UNA NUEVA MASCOTA
    MascotasDTO crearMascotas(MascotasDTO mascotasDTO);

    //ACTUALIZAR UNA MASCOTA EXISTENTE
    MascotasDTO updateMascotas(Long IdMascota, MascotasDTO mascotasDTO);

    //ELIMINAR MASCOTAS POR ID
    void deleteMascotas (Long idMascota);
    List<MascotasDTO> obtenerMascotasPorDuenoId(Long duenoId);
    MascotasDTO crearMascota(MascotasDTO mascotaDTO);
    List<MascotasDTO>getAllMascotas();
}
