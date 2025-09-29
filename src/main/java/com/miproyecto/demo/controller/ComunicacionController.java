package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.service.EmailService;
import com.miproyecto.demo.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/comunicaciones")
public class ComunicacionController {

    @Autowired private UsuariosService usuariosService;
    @Autowired private EmailService emailService;
    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private VeterinarioRepository veterinarioRepository;

    @GetMapping
    public String mostrarPaginaComunicaciones(Model model, Authentication auth,
                                              @RequestParam(required = false) String nombre,
                                              @RequestParam(required = false) String apellido,
                                              @RequestParam(required = false) String correo) {
        // --- LÓGICA QUE FALTABA AÑADIDA ---
        Veterinarios veterinario = getVeterinarioLogueado(auth);
        List<UsuariosDTO> duenos = usuariosService.findDuenosByVeterinarioId(veterinario.getIdVeterinario(), nombre, apellido, correo);
        // ------------------------------------

        model.addAttribute("duenos", duenos);
        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("correo", correo);

        return "veterinarios/comunicaciones";
    }

    @PostMapping("/enviar")
    public String enviarCorreoMasivo(@RequestParam(value = "destinatarios", required = false) List<Long> destinatariosIds,
                                     @RequestParam("asunto") String asunto,
                                     @RequestParam("mensaje") String mensaje,
                                     @RequestParam("adjunto") MultipartFile adjunto,
                                     RedirectAttributes redirectAttributes) {

        if (destinatariosIds == null || destinatariosIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Debes seleccionar al menos un destinatario.");
            return "redirect:/comunicaciones";
        }

        try {
            String[] correos = usuariosService.findAllByIds(destinatariosIds).stream()
                    .map(UsuariosDTO::getCorreo)
                    .toArray(String[]::new);

            emailService.enviarCorreoMasivo(correos, asunto, mensaje, adjunto);
            redirectAttributes.addFlashAttribute("successMessage", "Correo masivo enviado exitosamente a " + correos.length + " destinatarios.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al enviar el correo: " + e.getMessage());
        }

        return "redirect:/comunicaciones";
    }

    // --- MÉTODO HELPER AÑADIDO ---
    private Veterinarios getVeterinarioLogueado(Authentication auth) {
        Usuarios usuario = usuariosRepository.findBycorreo(auth.getName()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return veterinarioRepository.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Perfil de veterinario no encontrado"));
    }
}