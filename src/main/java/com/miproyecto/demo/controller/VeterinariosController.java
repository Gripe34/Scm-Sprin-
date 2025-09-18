package com.miproyecto.demo.controller;


import com.miproyecto.demo.dto.VeterinariosDTO;
import com.miproyecto.demo.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/veterinarios")
public class VeterinariosController {

    private final VeterinarioService veterinarioService;



    @Autowired
    public VeterinariosController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }
    @GetMapping("/index") // Puedes usar "/index" o "/listar", como prefieras
    public String mostrarIndexDeVeterinarios(Model model) {
        // 1. Pide la lista completa de veterinarios al servicio.
        List<VeterinariosDTO> listaDeVeterinarios = veterinarioService.findAllVeterinarios();

        // 2. Agrega esa lista al modelo con el nombre "veterinarios".
        //    Este es el nombre que tu HTML usará para acceder a los datos.
        model.addAttribute("veterinarios", listaDeVeterinarios);

        // 3. Devuelve el nombre del archivo HTML que quieres mostrar.
        //    Esto cargará tu archivo en: templates/veterinarios/index.html
        return "veterinarios/index";
    }

    // Obtener todos los veterinarios
    @GetMapping("/all")
    public ResponseEntity<List<VeterinariosDTO>> getAllVeterinarios() {
        List<VeterinariosDTO> veterinarios = veterinarioService.findAllVeterinarios();
        return ResponseEntity.ok(veterinarios);
    }

    // Obtener veterinario por ID
    @GetMapping("/{id}")
    public ResponseEntity<VeterinariosDTO> getVeterinarioById(@PathVariable Long id) {
        VeterinariosDTO veterinario = veterinarioService.getVeterionarioById(id);
        return ResponseEntity.ok(veterinario);
    }

    // Crear nuevo veterinario
    @PostMapping
    public ResponseEntity<VeterinariosDTO> crearVeterinario(@RequestBody VeterinariosDTO dto) {
        VeterinariosDTO nuevoVeterinario = veterinarioService.crearVeterinario(dto);
        return ResponseEntity.ok(nuevoVeterinario);
    }

    // Actualizar veterinario existente
    @PutMapping("/{id}")
    public ResponseEntity<VeterinariosDTO> actualizarVeterinario(@PathVariable Long id, @RequestBody VeterinariosDTO dto) {
        VeterinariosDTO actualizado = veterinarioService.updateVeterinario(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar veterinario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(@PathVariable Long id) {
        veterinarioService.deleteVeterinario(id);
        return ResponseEntity.noContent().build();
    }


    //Mostrar formulario de crear
    @GetMapping("/crear")
    public String mostrarFormularioCrearVeterinario(Model model) {
        model.addAttribute("veterinario", new VeterinariosDTO());
        return "veterinarios/crear";
    }


    //Procesar formulario de crear
    @PostMapping("/crear")
    public String crearVeterinarioCompleto(@ModelAttribute("veterinario") VeterinariosDTO veterinarioDTO, RedirectAttributes redirectAttributes) {
        try {
            veterinarioService.crearVeterinario(veterinarioDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Veterinario creado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el veterinario: " + e.getMessage());
        }
        return "redirect:/veterinarios/listar"; // Redirecciona a la lista de veterinarios
    }


    //LISTAR VETERINARIO
    @GetMapping("/listar")
    public String ListarVeterinarios(Model model) {
      //  model.addAttribute("veterinarios", veterinarioService.findAllVeterinarios());
        return "veterinarios/listar"; // templates/veterinarios/listar.html
    }





}
