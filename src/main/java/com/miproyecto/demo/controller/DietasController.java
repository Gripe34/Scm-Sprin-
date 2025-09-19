package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.DietasDTO;
import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.DietasService;
import com.miproyecto.demo.service.MascotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dietas") // URL base para todas las peticiones de este controlador
public class DietasController {

    private final DietasService dietasService;
    private final MascotasService mascotasService;
    private final UsuariosRepository usuariosRepository;

    @Autowired
    public DietasController(DietasService dietasService, MascotasService mascotasService, UsuariosRepository usuariosRepository) {
        this.dietasService = dietasService;
        this.mascotasService = mascotasService;
        this.usuariosRepository = usuariosRepository;
    }

    // --- MÉTODOS PARA LAS VISTAS (MVC) ---

    /**
     * Muestra una página con todas las mascotas del dueño actual para que elija una.
     * Esta es la primera página que el usuario verá al hacer clic en "Dietas".
     * URL: /dietas/seleccionar-mascota
     */
    @GetMapping("/seleccionar-mascota")
    public String mostrarMascotasParaDieta(Model model, Authentication auth) {
        // Obtenemos el usuario que ha iniciado sesión
        String email = auth.getName();
        Usuarios usuario = usuariosRepository.findBycorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la sesión"));

        // Buscamos sus mascotas y las añadimos al modelo
        List<MascotasDTO> misMascotas = mascotasService.obtenerMascotasPorDuenoId(usuario.getIdUsuario());
        model.addAttribute("mascotas", misMascotas);

        // Devolvemos el nombre del archivo JSP para renderizar
        return "cliente/seleccionarMascotaParaDieta";
    }

    /**
     * Muestra la lista de dietas para una mascota específica.
     * Se activa cuando el usuario hace clic en el botón "Ver Dieta" de una mascota.
     * URL: /dietas/mascota/{idMascota}
     */
    @GetMapping("/mascota/{idMascota}")
    public String verDietasDeMascota(@PathVariable Long idMascota, Model model) {
        // Usamos el servicio para obtener la lista de dietas por el ID de la mascota
        List<DietasDTO> dietas = dietasService.findDietasByMascotaId(idMascota);
        model.addAttribute("dietas", dietas); // La vista JSP usará esta lista

        // Devolvemos el nombre del archivo JSP de la plantilla
        return "dietasPorMascota";
    }


    // --- ENDPOINTS PARA LA API (REST) ---

    /**
     * Obtiene todas las dietas (formato JSON).
     * URL: /dietas/api/all
     */
    @GetMapping("/api/all")
    public ResponseEntity<List<DietasDTO>> getAllDietas() {
        List<DietasDTO> dietas = dietasService.findAllDietas();
        return ResponseEntity.ok(dietas);
    }

    /**
     * Obtiene una dieta por su ID (formato JSON).
     * URL: /dietas/api/{id}
     */
    @GetMapping("/api/{id}")
    public ResponseEntity<DietasDTO> getDietaById(@PathVariable Long id) {
        DietasDTO dieta = dietasService.getDietasById(id);
        return ResponseEntity.ok(dieta);
    }

    /**
     * Crea una nueva dieta (recibe JSON, devuelve JSON).
     * URL: /dietas/api/crear
     */
    @PostMapping("/api/crear")
    public ResponseEntity<DietasDTO> crearDieta(@RequestBody DietasDTO dto) {
        DietasDTO nuevaDieta = dietasService.crearDietas(dto);
        return ResponseEntity.ok(nuevaDieta);
    }

    /**
     * Actualiza una dieta existente (recibe JSON, devuelve JSON).
     * URL: /dietas/api/actualizar/{id}
     */
    @PutMapping("/api/actualizar/{id}")
    public ResponseEntity<DietasDTO> actualizarDieta(@PathVariable Long id, @RequestBody DietasDTO dto) {
        DietasDTO actualizada = dietasService.updateDietas(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina una dieta por su ID.
     * URL: /dietas/api/eliminar/{id}
     */
    @DeleteMapping("/api/eliminar/{id}")
    public ResponseEntity<Void> eliminarDieta(@PathVariable Long id) {
        dietasService.deleteDietas(id);
        return ResponseEntity.noContent().build(); // Retorna un código 204 (sin contenido)
    }
}