package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.DietasDTO;
import com.miproyecto.demo.service.DietasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dietas")
public class DietasController {

    private final DietasService dietasService;

    @Autowired
    public DietasController(DietasService dietasService) {
        this.dietasService = dietasService;
    }

    // Obtener todas las dietas
    @GetMapping("/all")
    public ResponseEntity<List<DietasDTO>> getAllDietas() {
        List<DietasDTO> dietas = dietasService.findAllDietas();
        return ResponseEntity.ok(dietas);
    }

    // Obtener dieta por ID
    @GetMapping("/{id}")
    public ResponseEntity<DietasDTO> getDietaById(@PathVariable Long id) {
        DietasDTO dieta = dietasService.getDietasById(id);
        return ResponseEntity.ok(dieta);
    }

    // Crear nueva dieta
    @PostMapping("/create/{id}")
    public ResponseEntity<DietasDTO> crearDieta(@RequestBody DietasDTO dto) {
        DietasDTO nuevaDieta = dietasService.crearDietas(dto);
        return ResponseEntity.ok(nuevaDieta);
    }

    // Actualizar dieta existente
    @PutMapping("/update/{id}")
    public ResponseEntity<DietasDTO> actualizarDieta(@PathVariable Long id, @RequestBody DietasDTO dto) {
        DietasDTO actualizada = dietasService.updateDietas(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar dieta
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarDieta(@PathVariable Long id) {
        dietasService.deleteDietas(id);
        return ResponseEntity.noContent().build();
    }
}
