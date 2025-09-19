package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.CitasDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CitasService {

    List<CitasDTO> findAllCitas();

    CitasDTO getCitasById(Long idcita);

    CitasDTO crearCitas(CitasDTO citasoDTO);

    CitasDTO updateCitas(Long idcita, CitasDTO citasDTO);

    void deleteCitas(long idCita);
    List<CitasDTO> findCitasByVeterinario(Veterinarios veterinario);
    List<CitasDTO> findCitasByCliente(Usuarios cliente);
}
