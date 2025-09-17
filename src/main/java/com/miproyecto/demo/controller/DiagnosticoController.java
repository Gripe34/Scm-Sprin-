package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.service.DiagnosticoDuenoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    private final DiagnosticoDuenoService diagnosticoDuenoService;

    @Autowired
    public DiagnosticoController(DiagnosticoDuenoService diagnosticoDuenoService) {
        this.diagnosticoDuenoService = diagnosticoDuenoService;
    }

    // Obtener todos los diagnósticos
    @GetMapping("/all")
    public ResponseEntity<List<DiagnosticoDuenoDTO>> getAllDiagnosticos() {
        List<DiagnosticoDuenoDTO> diagnosticos = diagnosticoDuenoService.findAlldiagnosticoDueno();
        return ResponseEntity.ok(diagnosticos);
    }

    // Obtener diagnóstico por ID
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoDuenoDTO> getDiagnosticoById(@PathVariable Long id) {
        DiagnosticoDuenoDTO diagnostico = diagnosticoDuenoService.getDiagnosticoDuenoById(id);
        return ResponseEntity.ok(diagnostico);
    }

    // Crear nuevo diagnóstico
    @PostMapping
    public ResponseEntity<DiagnosticoDuenoDTO> crearDiagnostico(@RequestBody DiagnosticoDuenoDTO dto) {
        DiagnosticoDuenoDTO nuevoDiagnostico = diagnosticoDuenoService.crearDiagnostico(dto);
        return ResponseEntity.ok(nuevoDiagnostico);
    }

    // Actualizar diagnóstico existente
    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoDuenoDTO> actualizarDiagnostico(@PathVariable Long id, @RequestBody DiagnosticoDuenoDTO dto) {
        DiagnosticoDuenoDTO actualizado = diagnosticoDuenoService.updateDiagnostico(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar diagnóstico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDiagnostico(@PathVariable Long id) {
        diagnosticoDuenoService.deleteDiagnostico(id);
        return ResponseEntity.noContent().build();
    }
}
