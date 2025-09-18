package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.dto.MascotasDTO; // Asegúrate de importar esto
import com.miproyecto.demo.dto.VeterinariosDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.MascotasService; // Y esto
import com.miproyecto.demo.service.VeterinarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired; // Puedes añadir @Autowired si quieres ser explícito
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final UsuariosRepository usuariosRepositorio;
    private final MascotasService mascotaService;
    private  final VeterinarioService veterinarioService;// <- 1. Elimina la inicialización a null

    // 2. SOLUCIÓN: Añade MascotasService como parámetro del constructor
    @Autowired
    public ClienteController(UsuariosRepository usuariosRepositorio, MascotasService mascotaService, VeterinarioService veterinarioService) {
        this.usuariosRepositorio = usuariosRepositorio;
        this.mascotaService = mascotaService; // <- 3. Asígnale el valor que Spring te da
        this.veterinarioService = veterinarioService;
    }

    @GetMapping("/index")
    public String mostrarClienteDashboard(Model model, Authentication authentication, HttpSession session) {
        String email = authentication.getName();
        Usuarios usuario = usuariosRepositorio.findBycorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Filtra las mascotas del usuario actual
        List<MascotasDTO> todasLasMascotas = mascotaService.getAllMascotas();
        List<MascotasDTO> mascotasDelUsuario = todasLasMascotas.stream()
                .filter(m -> m.getIdUsuario().equals(usuario.getIdUsuario()))
                .toList();

        model.addAttribute("mascotas", mascotasDelUsuario);

        List<VeterinariosDTO> veterinarios = veterinarioService.findAllVeterinarios();
        model.addAttribute("veterinarios", veterinarios);

        model.addAttribute("usuario", usuario);
        model.addAttribute("logueado", true);
        model.addAttribute("diagnosticoDTO", new DiagnosticoDuenoDTO());

        session.setAttribute("usuarioSesion", usuario);

        return "cliente/index";
    }
}