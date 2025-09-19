package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.CitasDTO;
import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.dto.VeterinariosDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.CitasService;
import com.miproyecto.demo.service.MascotasService;
import com.miproyecto.demo.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final UsuariosRepository usuariosRepository;
    private final MascotasService mascotaService;
    private final VeterinarioService veterinarioService;
    private final CitasService citasService;

    @Autowired
    public ClienteController(UsuariosRepository usuariosRepositorio, MascotasService mascotaService, VeterinarioService veterinarioService, CitasService citasService) {
        this.usuariosRepository = usuariosRepositorio;
        this.mascotaService = mascotaService;
        this.veterinarioService = veterinarioService;
        this.citasService = citasService;
    }

    /**
     * ESTE MÉTODO ES LA SOLUCIÓN.
     * Se ejecuta ANTES de cualquier @GetMapping en este controlador.
     * Prepara y añade todos los datos comunes al modelo.
     */
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
                List<MascotasDTO> mascotasDelUsuario = mascotaService.obtenerMascotasPorDuenoId(usuario.getIdUsuario());
                model.addAttribute("mascotas", mascotasDelUsuario);
            }

            // Añade la lista de todos los veterinarios
            List<VeterinariosDTO> veterinarios = veterinarioService.findAllVeterinarios();
            model.addAttribute("veterinarios", veterinarios);

            // Añade el DTO vacío para el modal de diagnóstico
            model.addAttribute("diagnosticoDTO", new DiagnosticoDuenoDTO());
        }
    }

    /**
     * El método del dashboard ahora es súper simple.
     * Solo tiene que devolver el nombre de la vista.
     */
    @GetMapping("/index")
    public String mostrarClienteDashboard() {
        // Ya no necesitas añadir nada al modelo aquí.
        // El método addCommonAttributes() ya hizo todo el trabajo.
        return "cliente/index";
    }

    /**
     * El método para ver las citas también se simplifica.
     * Solo se encarga de su lógica específica: buscar las citas.
     */
    @GetMapping("/mis-citas")
    public String mostrarMisCitas(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuarios usuario = usuariosRepository.findBycorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<CitasDTO> citas = citasService.findCitasByCliente(usuario);
        model.addAttribute("listaCitas", citas);

        return "cliente/citas";
    }
}