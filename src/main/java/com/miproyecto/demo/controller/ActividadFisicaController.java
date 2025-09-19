package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.ActividadFisicaService;
import com.miproyecto.demo.service.MascotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/actividades")
public class ActividadFisicaController {

    private final ActividadFisicaService actividadService;
    private final MascotasService mascotasService;
    private final UsuariosRepository usuariosRepository;

    @Autowired
    public ActividadFisicaController(ActividadFisicaService actividadService, MascotasService mascotasService, UsuariosRepository usuariosRepository) {
        this.actividadService = actividadService;
        this.mascotasService = mascotasService;
        this.usuariosRepository = usuariosRepository;
    }

    @GetMapping("/seleccionar-mascota")
    public String mostrarMascotasParaActividad(Model model, Authentication auth) {
        String email = auth.getName();
        Usuarios usuario = usuariosRepository.findBycorreo(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<MascotasDTO> misMascotas = mascotasService.obtenerMascotasPorDuenoId(usuario.getIdUsuario());
        model.addAttribute("mascotas", misMascotas);
        return "cliente/seleccionarMascotaParaActividad"; // Vista de selección
    }

    @GetMapping("/mascota/{idMascota}")
    public String verActividadesDeMascota(@PathVariable Long idMascota, Model model) {
        List<ActividadFisicaDTO> actividades = actividadService.findActividadesByMascotaId(idMascota);
        model.addAttribute("actividades", actividades);
        return "actividades/actividadesPorMascota.html"; // Vista final
    }
}
