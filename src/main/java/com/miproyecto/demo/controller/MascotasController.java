package com.miproyecto.demo.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.dto.VeterinariosDTO;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.MascotasService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@Controller
public class MascotasController {

    private final MascotasService mascotasService;
    private final UsuariosRepository usuariosRepository;
    private final com.miproyecto.demo.service.VeterinarioService veterinarioService;

    @Autowired
    public MascotasController(MascotasService mascotasService,
                              UsuariosRepository usuariosRepository,
                              com.miproyecto.demo.service.VeterinarioService veterinarioService) {
        this.mascotasService = mascotasService;
        this.usuariosRepository = usuariosRepository;
        this.veterinarioService = veterinarioService;
    }

    // ------------------- ATRIBUTOS GLOBALES ---------------------

    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Usuarios usuario = usuariosRepository.findBycorreo(email).orElse(null);

            if (usuario != null) {
                model.addAttribute("usuario", usuario);
                model.addAttribute("logueado", true);

                List<MascotasDTO> mascotasDelUsuario =
                        mascotasService.obtenerMascotasPorDuenoId(usuario.getIdUsuario());
                model.addAttribute("mascotas", mascotasDelUsuario);
            }

            List<VeterinariosDTO> veterinarios = veterinarioService.findAllVeterinarios();
            model.addAttribute("veterinarios", veterinarios);
            model.addAttribute("diagnosticoDTO", new DiagnosticoDuenoDTO());
        }
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

    // ------------------- API CRUD ---------------------

    @GetMapping("/api/mascotas/all")
    public ResponseEntity<List<MascotasDTO>> getAllMascotas() {
        return ResponseEntity.ok(mascotasService.findAllMascotas());
    }

    @GetMapping("/api/mascotas/{id}")
    public ResponseEntity<MascotasDTO> getMascotaById(@PathVariable Long id) {
        return ResponseEntity.ok(mascotasService.getMascotasById(id));
    }

    @PostMapping("/api/mascotas/Create")
    public ResponseEntity<MascotasDTO> crearMascota(@RequestBody MascotasDTO dto) {
        return ResponseEntity.ok(mascotasService.crearMascotas(dto));
    }

    @PutMapping("/api/mascotas/update/{id}")
    public ResponseEntity<MascotasDTO> actualizarMascota(@PathVariable Long id, @RequestBody MascotasDTO dto) {
        return ResponseEntity.ok(mascotasService.updateMascotas(id, dto));
    }

    @DeleteMapping("/api/mascotas/delete/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        mascotasService.deleteMascotas(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- VISTAS ---------------------

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

    @GetMapping("mascotas/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("mascotaDTO", new MascotasDTO());
        return "mascotas/crear";
    }

    @PostMapping("mascotas/crear")
    public String crearMascota(@ModelAttribute MascotasDTO mascotaDTO,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
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

    // ------------------- DESCARGAR PDF ---------------------

    @GetMapping("/dueno/mascotas/{id}/ficha/pdf")
    public void exportarFichaMascota(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        MascotasDTO mascota = mascotasService.getMascotasById(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=ficha_mascota_" + mascota.getNombre() + ".pdf");

        try (OutputStream out = response.getOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // --- Título ---
            Font fontTitulo = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph titulo = new Paragraph("Ficha de Mascota", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // --- Imagen ---
            if (mascota.getFoto() != null) {
                String rutaImagen = "uploads/mascotas/" + mascota.getFoto();
                try {
                    Image imagen = Image.getInstance(rutaImagen);
                    imagen.scaleToFit(150, 150);
                    imagen.setAlignment(Element.ALIGN_CENTER);
                    document.add(imagen);
                    document.add(new Paragraph(" "));
                } catch (Exception e) {
                    document.add(new Paragraph("⚠ No se pudo cargar la imagen de la mascota."));
                }
            }

            // --- Tabla de datos ---
            Font fontHeader = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font fontNormal = new Font(Font.HELVETICA, 12);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(80);
            tabla.setSpacingBefore(20);
            tabla.setSpacingAfter(20);

            tabla.addCell(new Phrase("Nombre:", fontHeader));
            tabla.addCell(new Phrase(mascota.getNombre(), fontNormal));

            tabla.addCell(new Phrase("Raza:", fontHeader));
            tabla.addCell(new Phrase(mascota.getRaza(), fontNormal));

            tabla.addCell(new Phrase("Género:", fontHeader));
            tabla.addCell(new Phrase(mascota.getGenero(), fontNormal));

            tabla.addCell(new Phrase("Fecha de Nacimiento:", fontHeader));
            tabla.addCell(new Phrase(String.valueOf(mascota.getFechaNacimiento()), fontNormal));

            document.add(tabla);

            document.close();
        }
    }
}

