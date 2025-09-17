package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Citas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitasRepository extends JpaRepository<Citas, Long> {
    Citas findByIdCita(Long idCita);
}
