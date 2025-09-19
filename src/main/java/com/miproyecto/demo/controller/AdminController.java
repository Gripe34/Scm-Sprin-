package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.CitasRepository;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <-- 1. ¡IMPORTANTE AÑADIR ESTE IMPORT!
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UsuariosRepository usuarioRepo;
    @Autowired private MascotasRepository mascotaRepo;
    @Autowired private CitasRepository citaRepo;
    @Autowired private VeterinarioRepository veterinarioRepo;


    @GetMapping("/index")
    public String mostrarAdminDashboard(Model model) {


        model.addAttribute("totalUsuarios", usuarioRepo.count());
        model.addAttribute("totalMascotas", mascotaRepo.count());

        model.addAttribute("mascotas", mascotaRepo.findAll());
        model.addAttribute("veterinarios", veterinarioRepo.findAll());
        model.addAttribute("diagnosticoDTO", new DiagnosticoDuenoDTO());


        return "admin/index";
    }

    @GetMapping("/gestionar-usuarios")
    public String gestionarUsuarios(Model model) {
        List<Usuarios> listaUsuarios = usuarioRepo.findAll();
        model.addAttribute("usuarios", listaUsuarios);
        return "admin/gestionar-usuarios";
    }

    @GetMapping("/gestionar-mascotas")
    public String gestionarMascotas(Model model) {
        model.addAttribute("mascotas", mascotaRepo.findAll());
        return "admin/gestionar-mascotas";
    }

    @GetMapping("/gestionar-veterinarios")
    public String gestionarVeterinarios(Model model) {
        model.addAttribute("veterinarios", veterinarioRepo.findAll());
        return "admin/gestionar-veterinarios";
    }


}