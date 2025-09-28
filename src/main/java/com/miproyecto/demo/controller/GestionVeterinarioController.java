package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import com.miproyecto.demo.dto.DietasDTO;
import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gestion")
public class GestionVeterinarioController {

    private final MascotasService mascotasService;
    private final DietasService dietasService;
    private final ActividadFisicaService actividadFisicaService;
    private final UsuariosService usuariosService;
    private final UsuariosRepository usuariosRepository;
    private final VeterinarioRepository veterinarioRepository;

    @Autowired
    public GestionVeterinarioController(MascotasService mascotasService, DietasService dietasService, ActividadFisicaService actividadFisicaService, UsuariosService usuariosService, UsuariosRepository usuariosRepository, VeterinarioRepository veterinarioRepository) {
        this.mascotasService = mascotasService;
        this.dietasService = dietasService;
        this.actividadFisicaService = actividadFisicaService;
        this.usuariosService = usuariosService;
        this.usuariosRepository = usuariosRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    // --- FLUJO PARA DIETAS ---

    // Paso 1: Muestra la lista de dueños de pacientes
    @GetMapping("/dietas/duenos")
    public String mostrarDuenosParaDieta(Model model, Authentication auth,
                                         @RequestParam(required = false) String nombre,
                                         @RequestParam(required = false) String apellido,
                                         @RequestParam(required = false) String correo) {
        Veterinarios veterinario = getVeterinarioLogueado(auth);
        List<UsuariosDTO> duenos = usuariosService.findDuenosByVeterinarioId(veterinario.getIdVeterinario(), nombre, apellido, correo);

        model.addAttribute("duenos", duenos);
        model.addAttribute("tipoGestion", "dietas");
        // Devolvemos los parámetros a la vista para que los campos no se borren
        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("correo", correo);
        return "veterinarios/listarDuenos";
    }

    // Paso 2: Muestra las mascotas del dueño seleccionado
    @GetMapping("/dietas/duenos/{idDueno}")
    public String mostrarMascotasDeDuenoParaDieta(@PathVariable Long idDueno, Model model) {
        List<MascotasDTO> mascotas = mascotasService.obtenerMascotasPorDuenoId(idDueno);
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("tipoGestion", "dietas");
        return "veterinarios/listarMascotasDeDueno";
    }

    // Paso 3: Muestra el formulario para crear la dieta
    @GetMapping("/dietas/crear/{idMascota}")
    public String mostrarFormularioCrearDieta(@PathVariable Long idMascota, Model model, Authentication auth) {
        Veterinarios veterinario = getVeterinarioLogueado(auth);
        MascotasDTO mascota = mascotasService.getMascotasById(idMascota);
        DietasDTO dietaDTO = new DietasDTO();
        dietaDTO.setIdMascota(idMascota);
        dietaDTO.setIdVeterinario(veterinario.getIdVeterinario());
        model.addAttribute("dietaDTO", dietaDTO);
        model.addAttribute("mascota", mascota);
        return "veterinarios/formularioDieta";
    }

    // Paso 4: Procesa el envío del formulario
    @PostMapping("/dietas/crear")
    public String procesarFormularioCrearDieta(@ModelAttribute("dietaDTO") DietasDTO dietaDTO, RedirectAttributes redirectAttributes) {
        dietasService.crearDietas(dietaDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Dieta creada y asignada exitosamente.");
        return "redirect:/gestion/dietas/duenos";
    }


    // --- FLUJO PARA ACTIVIDAD FÍSICA ---

    // Paso 1: Muestra la lista de dueños de pacientes
    @GetMapping("/actividades/duenos")
    public String mostrarDuenosParaActividad(Model model, Authentication auth,
                                             @RequestParam(required = false) String nombre,
                                             @RequestParam(required = false) String apellido,
                                             @RequestParam(required = false) String correo) {

        // 1. Obtiene el veterinario que ha iniciado sesión
        Veterinarios veterinario = getVeterinarioLogueado(auth);

        // 2. Llama al servicio pasándole todos los filtros (pueden ser nulos o vacíos)
        List<UsuariosDTO> duenos = usuariosService.findDuenosByVeterinarioId(veterinario.getIdVeterinario(), nombre, apellido, correo);

        // 3. Agrega los resultados y los parámetros de búsqueda al modelo
        model.addAttribute("duenos", duenos);
        model.addAttribute("tipoGestion", "actividades");
        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("correo", correo);

        // 4. Devuelve la vista que mostrará la lista de dueños
        return "veterinarios/listarDuenos";
    }

    // Paso 2: Muestra las mascotas del dueño seleccionado
    @GetMapping("/actividades/duenos/{idDueno}")
    public String mostrarMascotasDeDuenoParaActividad(@PathVariable Long idDueno, Model model) {
        List<MascotasDTO> mascotas = mascotasService.obtenerMascotasPorDuenoId(idDueno);
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("tipoGestion", "actividades");
        return "veterinarios/listarMascotasDeDueno";
    }

    // Paso 3: Muestra el formulario para crear la actividad
    @GetMapping("/actividades/crear/{idMascota}")
    public String mostrarFormularioCrearActividad(@PathVariable Long idMascota, Model model, Authentication auth) {
        Veterinarios veterinario = getVeterinarioLogueado(auth);
        MascotasDTO mascota = mascotasService.getMascotasById(idMascota);
        ActividadFisicaDTO actividadDTO = new ActividadFisicaDTO();
        actividadDTO.setIdMascota(idMascota);
        actividadDTO.setIdVeterinario(veterinario.getIdVeterinario());
        model.addAttribute("actividadDTO", actividadDTO);
        model.addAttribute("mascota", mascota);
        return "veterinarios/formularioActividad";
    }

    // Paso 4: Procesa el envío del formulario
    @PostMapping("/actividades/crear") // <-- Se quitó "/gestion" del inicio
    public String procesarFormularioCrearActividad(@ModelAttribute("actividadDTO") ActividadFisicaDTO actividadDTO, RedirectAttributes redirectAttributes) {
        try {
            actividadFisicaService.crearActividadFisica(actividadDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Plan de actividad creado y asignado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear la actividad: " + e.getMessage());
        }
        return "redirect:/gestion/actividades/duenos";
    }

    // --- Método Helper ---
    private Veterinarios getVeterinarioLogueado(Authentication auth) {
        Usuarios usuario = usuariosRepository.findBycorreo(auth.getName()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return veterinarioRepository.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Perfil de veterinario no encontrado"));
    }
    @Autowired
    private PdfGenerationService pdfGenerationService; // Inyecta el nuevo servicio
    @GetMapping("/duenos/reporte/pdf")
    public ResponseEntity<byte[]> generarReportePdf(Authentication auth,
                                                    @RequestParam(required = false) String nombre,
                                                    @RequestParam(required = false) String apellido,
                                                    @RequestParam(required = false) String correo) {
        // 1. Obtiene los mismos datos filtrados que la vista de lista
        Veterinarios veterinario = getVeterinarioLogueado(auth);
        List<UsuariosDTO> duenos = usuariosService.findDuenosByVeterinarioId(veterinario.getIdVeterinario(), nombre, apellido, correo);

        // 2. Prepara los datos para la plantilla
        Map<String, Object> datosParaPdf = new HashMap<>();
        datosParaPdf.put("duenos", duenos);

        // 3. Llama al servicio para generar el PDF
        byte[] pdfBytes = pdfGenerationService.generarPdfDesdeHtml("reportes/reporteDuenosMascotas", datosParaPdf);

        // 4. Prepara las cabeceras para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_duenos.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}