package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.CitasDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CitasService {

    List<CitasDTO> findAllCitas();

    CitasDTO getCitasById(Long idcita);

    CitasDTO crearCitas(CitasDTO citasoDTO);

    CitasDTO updateCitas(Long idcita, CitasDTO citasDTO);

    void deleteCitas(long idCita);
}
