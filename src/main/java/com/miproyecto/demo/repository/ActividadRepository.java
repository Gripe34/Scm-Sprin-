package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.ActividadFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepository extends JpaRepository<ActividadFisica, Long> {
    ActividadFisica findById(long idActividadF);
}
