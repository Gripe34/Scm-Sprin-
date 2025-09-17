package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.service.MascotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotasController {

    private final MascotasService mascotasService;

    @Autowired
    public MascotasController(MascotasService mascotasService) {
        this.mascotasService = mascotasService;
    }

    // Obtener todas las mascotas
    @GetMapping("/all")
    public ResponseEntity<List<MascotasDTO>> getAllMascotas() {
        List<MascotasDTO> mascotas = mascotasService.findAllMascotas();
        return ResponseEntity.ok(mascotas);
    }

    // Obtener mascota por ID
    @GetMapping("/{id}")
    public ResponseEntity<MascotasDTO> getMascotaById(@PathVariable Long id) {
        MascotasDTO mascota = mascotasService.getMascotasById(id);
        return ResponseEntity.ok(mascota);
    }

    // Crear nueva mascota
    @PostMapping("Create")
    public ResponseEntity<MascotasDTO> crearMascota(@RequestBody MascotasDTO dto) {
        MascotasDTO nuevaMascota = mascotasService.crearMascotas(dto);
        return ResponseEntity.ok(nuevaMascota);
    }

    // Actualizar mascota existente
    @PutMapping("/update/{id}")
    public ResponseEntity<MascotasDTO> actualizarMascota(@PathVariable Long id, @RequestBody MascotasDTO dto) {
        MascotasDTO actualizada = mascotasService.updateMascotas(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar mascota
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        mascotasService.deleteMascotas(id);
        return ResponseEntity.noContent().build();
    }
}
