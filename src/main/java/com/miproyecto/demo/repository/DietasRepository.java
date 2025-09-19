package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Dietas;
import com.miproyecto.demo.entity.Mascotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface DietasRepository extends JpaRepository<Dietas, Long> {

    /**
     * Este es el método correcto.
     * Busca en la base de datos todas las entidades "Dietas"
     * que estén asociadas con un ID de mascota específico.
     */
    List<Dietas> findByMascota_IdMascota(Long idMascota);

}
