package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.DietasDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DietasService {

    List<DietasDTO> findAllDietas();

    DietasDTO getDietasById(Long idDieta);

    DietasDTO crearDietas(DietasDTO dietasDTO);

    DietasDTO updateDietas(Long idDieta, DietasDTO dietasDTO);

    void deleteDietas(Long idDieta);

    List<DietasDTO> findDietasByMascotaId(Long idMascota);
}
