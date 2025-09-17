package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import com.miproyecto.demo.service.ActividadFisicaService;
import org.hibernate.boot.jaxb.spi.Binding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/actividadF")
public class ActividadController {

    private final ActividadFisicaService actividadService;

    @Autowired
    public ActividadController(ActividadFisicaService actividadService) {
        this.actividadService = actividadService;
    }

    //Obtiene todas las actividades
    @GetMapping("/all")
    public ResponseEntity<List<ActividadFisicaDTO>> getAllActividadFisica() {
        List<ActividadFisicaDTO> actividades = actividadService.findAllActividadFisica();
        return ResponseEntity.ok(actividades);
    }

    //Obtiene una actividad por id
    @GetMapping("/{id}")
    public ResponseEntity<ActividadFisicaDTO> getActividadById(@PathVariable Long id) {
        ActividadFisicaDTO actividad = actividadService.getActividadById(id);
        return ResponseEntity.ok(actividad);
    }

    //Crea una nueva actividad
    @PostMapping("/Create")
    public ResponseEntity<ActividadFisicaDTO> crearActividad(@RequestBody ActividadFisicaDTO actividadDTO) {
        ActividadFisicaDTO nuevaActividad = actividadService.crearActividadFisica(actividadDTO);
        return ResponseEntity.ok(nuevaActividad);
    }

    //Actualiza una actividad existente
    @PutMapping("/update/{id}")
    public ResponseEntity<ActividadFisicaDTO> updateActividad(
            @PathVariable Long id,
            @RequestBody ActividadFisicaDTO actividadDTO) {
        ActividadFisicaDTO actividadActualizada = actividadService.updateActividadFisica(id, actividadDTO);
        return ResponseEntity.ok(actividadActualizada);
    }

    // Elimina una actividad por id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActividad(@PathVariable Long id) {
        actividadService.deleteActividadFisica(id);
        return ResponseEntity.noContent().build();
    }



    //VISTAS
    @GetMapping("/actividades")
    //CONTROLLER PARA VISTAS
    public String ActividadesView(Model model) {
        model.addAttribute("actividades", actividadService.findAllActividadFisica());
        return "actividades";
    }


    @GetMapping("/G/crear")
    public String crearActividadView(Model model) {
        model.addAttribute("actividad", new ActividadFisicaDTO());
        return "actividad/crud";
    }

    @PostMapping("/M/crear")
    public String crearActividad( @ModelAttribute("actividad") ActividadFisicaDTO actividadDTO,
        BindingResult result, RedirectAttributes redirect){
        if(result.hasErrors()){
            return "actividad/crud";
        }
        actividadService.crearActividadFisica(actividadDTO);
        redirect.addFlashAttribute("success", "Actividad creada con exito");
        return "redirect:/api/actividadF/actividades";
    }






    
}


