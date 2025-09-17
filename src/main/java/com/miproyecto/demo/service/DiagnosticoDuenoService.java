package com.miproyecto.demo.service;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DiagnosticoDuenoService {
    List<DiagnosticoDuenoDTO> findAlldiagnosticoDueno();

    DiagnosticoDuenoDTO getDiagnosticoDuenoById(Long idDiagnostico);

    DiagnosticoDuenoDTO crearDiagnostico(DiagnosticoDuenoDTO diagnosticoDuenoDTO);

    DiagnosticoDuenoDTO updateDiagnostico(Long IdDiagnostico, DiagnosticoDuenoDTO diagnosticoDuenoDTO);

    void deleteDiagnostico(long idDiagnostico);
}
