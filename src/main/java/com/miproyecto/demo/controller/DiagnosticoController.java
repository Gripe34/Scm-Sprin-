package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.CitasDTO;
import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.service.DiagnosticoDuenoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DiagnosticoController {

    private final DiagnosticoDuenoService diagnosticoDuenoService;
     private final com.miproyecto.demo.repository.UsuariosRepository usuariosRepository;
    private final com.miproyecto.demo.repository.VeterinarioRepository veterinariosRepository;

    @Autowired
    public DiagnosticoController(DiagnosticoDuenoService diagnosticoDuenoService, com.miproyecto.demo.repository.UsuariosRepository usuariosRepository, com.miproyecto.demo.repository.VeterinarioRepository veterinariosRepository) {
        this.diagnosticoDuenoService = diagnosticoDuenoService;
        this.usuariosRepository = usuariosRepository;
        this.veterinariosRepository = veterinariosRepository;
    }

    // Obtener todos los diagnósticos
    @GetMapping("/api/diagnosticos/all")
    public ResponseEntity<List<DiagnosticoDuenoDTO>> getAllDiagnosticos() {
        List<DiagnosticoDuenoDTO> diagnosticos = diagnosticoDuenoService.findAlldiagnosticoDueno();
        return ResponseEntity.ok(diagnosticos);
    }

    // Obtener diagnóstico por ID
    @GetMapping("/api/diagnosticos/{id}")
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
    @PutMapping("/api/diagnosticos/{id}")
    public ResponseEntity<DiagnosticoDuenoDTO> actualizarDiagnostico(@PathVariable Long id, @RequestBody DiagnosticoDuenoDTO dto) {
        DiagnosticoDuenoDTO actualizado = diagnosticoDuenoService.updateDiagnostico(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar diagnóstico
    @DeleteMapping("/api/diagnosticos/{id}")
    public ResponseEntity<Void> eliminarDiagnostico(@PathVariable Long id) {
        diagnosticoDuenoService.deleteDiagnostico(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("diagnosticos/crear")
    public String crearDiagnostico(@ModelAttribute("diagnosticoDTO") DiagnosticoDuenoDTO diagnosticoDTO,
                                   RedirectAttributes redirectAttributes) {
        try {
            diagnosticoDuenoService.crearDiagnostico(diagnosticoDTO);
            // Si todo sale bien, guardamos un mensaje de éxito para mostrarlo después de redirigir
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Diagnóstico registrado con éxito!");
        } catch (Exception e) {
            // Si algo falla, guardamos un mensaje de error
            redirectAttributes.addFlashAttribute("mensajeError", "Error al registrar el diagnóstico: " + e.getMessage());
        }

        // Redirigimos de vuelta al dashboard del cliente
        return "redirect:/cliente/index";
    }



    @GetMapping("diagnostico/listar")
    public String listarDiagnosticos(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuarios usuarioLogueado = usuariosRepository.findBycorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Veterinarios veterinario = veterinariosRepository.findByUsuario(usuarioLogueado)
                .orElseThrow(() -> new RuntimeException("Perfil de veterinario no encontrado"));

        List<DiagnosticoDuenoDTO> diagnosticos = diagnosticoDuenoService.findDiagnosticosByVeterinario(veterinario);

        model.addAttribute("listaDiagnosticos", diagnosticos);
        model.addAttribute("citaDTO", new CitasDTO());

        return "diagnosticos/listar";
    }
}
