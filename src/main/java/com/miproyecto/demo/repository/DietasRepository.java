package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Dietas;
import com.miproyecto.demo.entity.Mascotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietasRepository extends JpaRepository<Dietas, Long> {
    Mascotas findByIdDieta(Long idDieta);
}
