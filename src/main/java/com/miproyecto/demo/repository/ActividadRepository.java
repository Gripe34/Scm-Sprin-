package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.ActividadFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<ActividadFisica, Long> {
    // Encuentra todas las actividades asociadas a un ID de mascota
    List<ActividadFisica> findByMascota_IdMascota(Long idMascota);
}
