package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.RolesDTO;
import com.miproyecto.demo.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final RolesService rolesService;

    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    // Obtener todos los roles
    @GetMapping("/all")
    public ResponseEntity<List<RolesDTO>> getAllRoles() {
        List<RolesDTO> roles = rolesService.findAllRoles();
        return ResponseEntity.ok(roles);
    }

    // Obtener rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<RolesDTO> getRolById(@PathVariable Long id) {
        RolesDTO rol = rolesService.getRolesById(id);
        return ResponseEntity.ok(rol);
    }

    // Crear nuevo rol
    @PostMapping
    public ResponseEntity<RolesDTO> crearRol(@RequestBody RolesDTO dto) {
        RolesDTO nuevoRol = rolesService.crearRoles(dto);
        return ResponseEntity.ok(nuevoRol);
    }

    // Actualizar rol existente
    @PutMapping("/{id}")
    public ResponseEntity<RolesDTO> actualizarRol(@PathVariable Long id, @RequestBody RolesDTO dto) {
        RolesDTO actualizado = rolesService.updateRoles(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar rol
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        rolesService.deleteRoles(id);
        return ResponseEntity.noContent().build();
    }
}
