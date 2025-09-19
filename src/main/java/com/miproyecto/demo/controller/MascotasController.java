package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.dto.VeterinariosDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.MascotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class MascotasController {

    private final MascotasService mascotasService;
    private  final UsuariosRepository usuariosRepository;
    private final com.miproyecto.demo.service.VeterinarioService veterinarioService;

    @Autowired
    public MascotasController(MascotasService mascotasService, UsuariosRepository usuariosRepository, com.miproyecto.demo.service.VeterinarioService veterinarioService) {
        this.mascotasService = mascotasService;
        this.usuariosRepository = usuariosRepository;
        this.veterinarioService = veterinarioService;
    }
    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Usuarios usuario = usuariosRepository.findBycorreo(email)
                    .orElse(null);

            if (usuario != null) {
                // Añade el usuario logueado
                model.addAttribute("usuario", usuario);
                model.addAttribute("logueado", true);

                // Añade la lista de mascotas del usuario
                List<MascotasDTO> mascotasDelUsuario = mascotasService.obtenerMascotasPorDuenoId(usuario.getIdUsuario());
                model.addAttribute("mascotas", mascotasDelUsuario);
            }

            // Añade la lista de todos los veterinarios
            List<VeterinariosDTO> veterinarios = veterinarioService.findAllVeterinarios();
            model.addAttribute("veterinarios", veterinarios);

            // Añade el DTO vacío para el modal de diagnóstico
            model.addAttribute("diagnosticoDTO", new DiagnosticoDuenoDTO());
        }
    }

    // Obtener todas las mascotas
    @GetMapping("/api/mascotas/all")
    public ResponseEntity<List<MascotasDTO>> getAllMascotas() {
        List<MascotasDTO> mascotas = mascotasService.findAllMascotas();
        return ResponseEntity.ok(mascotas);
    }

    // Obtener mascota por ID
    @GetMapping("/api/mascotas/{id}")
    public ResponseEntity<MascotasDTO> getMascotaById(@PathVariable Long id) {
        MascotasDTO mascota = mascotasService.getMascotasById(id);
        return ResponseEntity.ok(mascota);
    }

    // Crear nueva mascota
    @PostMapping("/api/mascotas/Create")
    public ResponseEntity<MascotasDTO> crearMascota(@RequestBody MascotasDTO dto) {
        MascotasDTO nuevaMascota = mascotasService.crearMascotas(dto);
        return ResponseEntity.ok(nuevaMascota);
    }

    // Actualizar mascota existente
    @PutMapping("/api/mascotas/update/{id}")
    public ResponseEntity<MascotasDTO> actualizarMascota(@PathVariable Long id, @RequestBody MascotasDTO dto) {
        MascotasDTO actualizada = mascotasService.updateMascotas(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar mascota
    @DeleteMapping("/api/mascotas/delete/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        mascotasService.deleteMascotas(id);
        return ResponseEntity.noContent().build();
    }
    @ModelAttribute
    public void agregarAtributosGlobales(Model model, Authentication auth) {
        model.addAttribute("diagnosticoDTO", new DiagnosticoDuenoDTO());
        model.addAttribute("logueado", auth != null && auth.isAuthenticated());

        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            Optional<Usuarios> optionalUsuario = usuariosRepository.findBycorreo(email);
            optionalUsuario.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        }
    }
    //CREAR metodos para las vistas
    // LISTAR MASCOTAS PARA EL DUEÑO DE MASCOTA
    @GetMapping("mascotas/listar")
    public String listarMascotas(Model model, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String emailUsuario = auth.getName();
        Usuarios usuario = usuariosRepository.findBycorreo(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario en sesión no encontrado"));

        List<MascotasDTO> mascotasDelUsuario = mascotasService.obtenerMascotasPorDuenoId(usuario.getIdUsuario());
        model.addAttribute("mascotas", mascotasDelUsuario);

        return "mascotas/listar";
    }

    // CREAR MASCOTAS PARA EL DUEÑO DE MASCOTA
    @GetMapping("mascotas/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("mascotaDTO", new MascotasDTO());
        return "mascotas/crear";
    }

    @PostMapping("mascotas/crear")
    public String crearMascota(@ModelAttribute MascotasDTO mascotaDTO, Authentication auth, RedirectAttributes redirectAttributes) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            String emailUsuario = auth.getName();
            Usuarios usuario = usuariosRepository.findBycorreo(emailUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario en sesión no encontrado"));

            mascotaDTO.setIdUsuario(usuario.getIdUsuario());
            mascotasService.crearMascota(mascotaDTO);

            return "redirect:/mascotas/listar";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la mascota: " + e.getMessage());
            return "redirect:/mascotas/crear";
        }
    }
}
