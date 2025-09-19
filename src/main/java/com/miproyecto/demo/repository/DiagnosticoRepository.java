package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.DiagnosticoDueno;
import com.miproyecto.demo.entity.Veterinarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticoRepository extends JpaRepository<DiagnosticoDueno, Long> {
    DiagnosticoDueno findByIdDiagnosticoDueno (Long idDiagnostico);
    List<DiagnosticoDueno> findByVeterinario(Veterinarios veterinario);
}
