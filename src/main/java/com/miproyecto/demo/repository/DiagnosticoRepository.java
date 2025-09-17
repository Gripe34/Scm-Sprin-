package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.DiagnosticoDueno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticoRepository extends JpaRepository<DiagnosticoDueno, Long> {
    DiagnosticoDueno findByIdDiagnosticoDueno (Long idDiagnostico);
}
