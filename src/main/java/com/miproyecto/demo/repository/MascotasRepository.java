package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Mascotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotasRepository extends JpaRepository<Mascotas, Long> {
    Mascotas findByIdMascota(Long idMascota);
    List<Mascotas> findByUsuario_IdUsuario(Long idUsuario);
    @Query("SELECT DISTINCT d.mascota FROM DiagnosticoDueno d WHERE d.veterinario.idVeterinario = :idVeterinario")
    List<Mascotas> findPacientesByVeterinarioId(@Param("idVeterinario") Long idVeterinario);
}


