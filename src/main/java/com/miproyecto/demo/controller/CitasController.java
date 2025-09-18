package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.CitasDTO;
import com.miproyecto.demo.service.CitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CitasController {

    private final CitasService citasService;

    @Autowired
    public CitasController(CitasService citasService) {
        this.citasService = citasService;
    }

    // Obtener todas las citas
    @GetMapping("/api/citas/all")
    public ResponseEntity<List<CitasDTO>> getAllCitas() {
        List<CitasDTO> citas = citasService.findAllCitas();
        return ResponseEntity.ok(citas);
    }

    // Obtener una cita por ID
    @GetMapping("/api/citas/{id}")
    public ResponseEntity<CitasDTO> getCitaById(@PathVariable Long id) {
        CitasDTO cita = citasService.getCitasById(id);
        if (cita != null) {
            return ResponseEntity.ok(cita);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear una nueva cita
    @PostMapping("/api/citas/Create")
    public ResponseEntity<CitasDTO> crearCita(@RequestBody CitasDTO citaDTO) {
        CitasDTO nuevaCita = citasService.crearCitas(citaDTO);
        return ResponseEntity.status(201).body(nuevaCita); // HTTP 201 para indicar creación
    }

    // Actualizar una cita existente
    @PutMapping("/api/citas/update/{id}")
    public ResponseEntity<CitasDTO> updateCita(@PathVariable Long id, @RequestBody CitasDTO citaDTO) {
        CitasDTO citaActualizada = citasService.updateCitas(id, citaDTO);
        if (citaActualizada != null) {
            return ResponseEntity.ok(citaActualizada);
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 si no se encuentra la cita
        }
    }

    // Eliminar una cita por ID
    @DeleteMapping("/api/citas/delete/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        try {
            citasService.deleteCitas(id); // Si no se encuentra, lanzar una excepción personalizada
            return ResponseEntity.noContent().build(); // HTTP 204 para éxito sin contenido
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // HTTP 404 si no se encuentra la cita
        }
    }
    @PostMapping("/citas/crear")
    public String crearCita(@ModelAttribute("citaDTO") CitasDTO citasDTO, RedirectAttributes redirectAttributes) {
        try {
            citasService.crearCitas(citasDTO);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Cita creada con éxito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al crear la cita: " + e.getMessage());
        }

        // Redirige a la lista de diagnósticos, donde estaba el usuario
        return "redirect:/diagnostico/listar";
    }

}
