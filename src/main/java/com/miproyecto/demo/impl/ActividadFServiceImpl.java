// En ActividadFServiceImpl.java
package com.miproyecto.demo.impl;

// ... (todas tus importaciones)

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import com.miproyecto.demo.entity.ActividadFisica;
import com.miproyecto.demo.repository.ActividadRepository;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.service.ActividadFisicaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadFServiceImpl implements ActividadFisicaService {

    private final ActividadRepository actividadRepository;
    private final MascotasRepository mascotasRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ActividadFServiceImpl(ActividadRepository actividadRepository,
                                 MascotasRepository mascotasRepository,
                                 VeterinarioRepository veterinarioRepository,
                                 ModelMapper modelMapper) {
        this.actividadRepository = actividadRepository;
        this.mascotasRepository = mascotasRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.modelMapper = modelMapper;
    }

    // ... (tus métodos CRUD existentes: findAll, getById, crear, update, delete) ...

    @Override
    public List<ActividadFisicaDTO> findAllActividadFisica() {
        return List.of();
    }

    @Override
    public ActividadFisicaDTO getActividadById(Long idActividadF) {
        return null;
    }

    @Override
    public ActividadFisicaDTO crearActividadFisica(ActividadFisicaDTO actividadFisicaDTO) {
        return null;
    }

    @Override
    public ActividadFisicaDTO updateActividadFisica(Long idActividadF, ActividadFisicaDTO actividadFisicaDTO) {
        return null;
    }

    @Override
    public void deleteActividadFisica(Long idActividadF) {

    }

    /**
     * Nuevo método para obtener las actividades de una mascota específica.
     */
    @Override
    public List<ActividadFisicaDTO> findActividadesByMascotaId(Long idMascota) {
        List<ActividadFisica> actividades = actividadRepository.findByMascota_IdMascota(idMascota);
        return actividades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Método helper para convertir la entidad a DTO, enriqueciendo con nombres.
     */
    private ActividadFisicaDTO convertirADTO(ActividadFisica actividad) {
        ActividadFisicaDTO dto = modelMapper.map(actividad, ActividadFisicaDTO.class);
        if (actividad.getMascota() != null) {
            dto.setNombreMascota(actividad.getMascota().getNombre());
        }
        if (actividad.getVeterinario() != null && actividad.getVeterinario().getUsuario() != null) {
            String nombreCompletoVet = actividad.getVeterinario().getUsuario().getNombre() + " " + actividad.getVeterinario().getUsuario().getApellido();
            dto.setNombreVeterinario(nombreCompletoVet);
        }
        return dto;
    }
}