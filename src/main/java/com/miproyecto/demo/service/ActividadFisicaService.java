package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ActividadFisicaService {

    List<ActividadFisicaDTO> findAllActividadFisica();

    ActividadFisicaDTO getActividadById(Long idActividadF);

    ActividadFisicaDTO crearActividadFisica(ActividadFisicaDTO actividadFisicaDTO);

    ActividadFisicaDTO updateActividadFisica(Long idActividadF, ActividadFisicaDTO actividadFisicaDTO);

    void deleteActividadFisica( Long idActividadF);

}

