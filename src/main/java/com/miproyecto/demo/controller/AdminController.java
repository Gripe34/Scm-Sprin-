package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.CitasRepository;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.service.UsuariosService;
import com.miproyecto.demo.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.miproyecto.demo.service.UsuariosService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UsuariosRepository usuarioRepo;
    @Autowired private MascotasRepository mascotaRepo;
    @Autowired private CitasRepository citaRepo;
    @Autowired private VeterinarioRepository veterinarioRepo;
    @Autowired private UsuariosService usuariosService;
    @Autowired private VeterinarioService veterinarioService;


    @GetMapping("/index")
    public String mostrarAdminDashboard(Model model) {


        model.addAttribute("totalUsuarios", usuarioRepo.count());
        model.addAttribute("totalMascotas", mascotaRepo.count());

        model.addAttribute("mascotas", mascotaRepo.findAll());
        model.addAttribute("veterinarios", veterinarioRepo.findAll());
        model.addAttribute("diagnosticoDTO", new DiagnosticoDuenoDTO());


        return "admin/index";
    }


    @GetMapping("/gestionar/{tipo}")
    public String gestionarUsuariosDinamico(@PathVariable String tipo, Model model, Pageable pageable) {
        Page<?> pagina;
        String titulo;

        if ("clientes".equals(tipo)) {
            // Lógica para obtener solo los clientes, no todos los usuarios
            pagina = usuariosService.obtenerUsuariosConPaginacion(pageable);
            titulo = "Gestión de Clientes";
        } else if ("veterinarios".equals(tipo)) {
            pagina = veterinarioService.obtenerVeterinariosConPaginacion(pageable);
            titulo = "Gestión de Veterinarios";
        } else {
            // Manejar error o redireccionar a una página por defecto
            return "redirect:/admin/index";
        }

        model.addAttribute("paginaDeUsuarios", pagina);
        model.addAttribute("titulo", titulo);
        model.addAttribute("tipoActual", tipo);

        return "admin/gestionar-usuarios";
    }

    @GetMapping("/gestionar-usuarios")
    public String gestionarUsuariosPorDefecto(Model model, Pageable pageable) {
        // La variable ahora coincide con el tipo de retorno del servicio
        Page<UsuariosDTO> pagina = usuariosService.obtenerUsuariosConPaginacion(pageable);
        String titulo = "Gestión de Clientes";
        String tipoActual = "clientes";

        model.addAttribute("paginaDeUsuarios", pagina);
        model.addAttribute("titulo", titulo);
        model.addAttribute("tipoActual", tipoActual);

        return "admin/gestionar-usuarios";
    }


}