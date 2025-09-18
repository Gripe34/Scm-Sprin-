package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Mascotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotasRepository extends JpaRepository<Mascotas, Long> {
    Mascotas findByIdMascota(Long idMascota);
    List<Mascotas> findByUsuario_IdUsuario(Long idUsuario);
}
