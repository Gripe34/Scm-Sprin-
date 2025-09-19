package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import com.miproyecto.demo.entity.ActividadFisica;
import com.miproyecto.demo.entity.Mascotas;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.repository.ActividadRepository;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.service.ActividadFisicaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Override
    public List<ActividadFisicaDTO> findAllActividadFisica() {
        return actividadRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActividadFisicaDTO getActividadById(Long idActividadF) {
        ActividadFisica actividad = actividadRepository.findById(idActividadF)
                .orElseThrow(() -> new RuntimeException("Actividad física no encontrada con id: " + idActividadF));
        return convertirADTO(actividad);
    }

    @Override
    public ActividadFisicaDTO crearActividadFisica(ActividadFisicaDTO actividadDTO) {
        // 1. Mapear los datos simples del DTO a la entidad
        ActividadFisica nuevaActividad = modelMapper.map(actividadDTO, ActividadFisica.class);

        // 2. Buscar las entidades RELACIONADAS usando los IDs del DTO
        Mascotas mascota = mascotasRepository.findById(actividadDTO.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + actividadDTO.getIdMascota()));

        Veterinarios veterinario = veterinarioRepository.findById(actividadDTO.getIdVeterinario())
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id: " + actividadDTO.getIdVeterinario()));

        // 3. Establecer las relaciones en la nueva entidad
        nuevaActividad.setMascota(mascota);
        nuevaActividad.setVeterinario(veterinario);

        // 4. Manejar la subida de la foto si existe
        if (actividadDTO.getArchivoFoto() != null && !actividadDTO.getArchivoFoto().isEmpty()) {
            String nombreFoto = guardarFotoActividad(actividadDTO.getArchivoFoto());
            nuevaActividad.setFoto(nombreFoto); // Asigna el nombre del archivo a la entidad
        }

        // 5. Guardar la entidad completa en la base de datos
        ActividadFisica actividadGuardada = actividadRepository.save(nuevaActividad);

        // 6. Devolver el DTO del objeto recién guardado
        return convertirADTO(actividadGuardada);
    }

    @Override
    public ActividadFisicaDTO updateActividadFisica(Long idActividadF, ActividadFisicaDTO actividadDTO) {
        ActividadFisica actividadExistente = actividadRepository.findById(idActividadF)
                .orElseThrow(() -> new RuntimeException("Actividad física no encontrada con id: " + idActividadF));

        // Actualizar campos
        actividadExistente.setTipoActividad(actividadDTO.getTipoActividad());
        actividadExistente.setDescripcion(actividadDTO.getDescripcion());

        // Manejar la foto si se sube una nueva
        if (actividadDTO.getArchivoFoto() != null && !actividadDTO.getArchivoFoto().isEmpty()) {
            String nombreFoto = guardarFotoActividad(actividadDTO.getArchivoFoto());
            actividadExistente.setFoto(nombreFoto);
        }

        ActividadFisica actividadActualizada = actividadRepository.save(actividadExistente);
        return convertirADTO(actividadActualizada);
    }

    @Override
    public void deleteActividadFisica(Long idActividadF) {
        if (!actividadRepository.existsById(idActividadF)) {
            throw new RuntimeException("No se puede eliminar. Actividad física no encontrada con id: " + idActividadF);
        }
        actividadRepository.deleteById(idActividadF);
    }

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
            dto.setIdMascota(actividad.getMascota().getIdMascota());
        }
        if (actividad.getVeterinario() != null) {
            dto.setIdVeterinario(actividad.getVeterinario().getIdVeterinario());
            if (actividad.getVeterinario().getUsuario() != null) {
                String nombreCompletoVet = actividad.getVeterinario().getUsuario().getNombre() + " " + actividad.getVeterinario().getUsuario().getApellido();
                dto.setNombreVeterinario(nombreCompletoVet);
            }
        }
        return dto;
    }

    /**
     * Método helper para guardar la foto de la actividad en el servidor.
     */
    private String guardarFotoActividad(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return null;
        }
        try {
            String nombreUnico = System.currentTimeMillis() + "_" + archivo.getOriginalFilename().replaceAll("\\s+", "_");
            Path rootLocation = Paths.get("uploads/actividades");
            Files.createDirectories(rootLocation);
            Files.copy(archivo.getInputStream(), rootLocation.resolve(nombreUnico));
            return nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la foto de la actividad: " + e.getMessage(), e);
        }
    }
}