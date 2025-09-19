package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.DietasDTO;
import com.miproyecto.demo.entity.Dietas;
import com.miproyecto.demo.entity.Mascotas;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.DietasRepository;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.service.DietasService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietasServiceImpl implements DietasService {

    private final DietasRepository dietasRepository;
    private final MascotasRepository mascotasRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DietasServiceImpl(DietasRepository dietasRepository,
                             MascotasRepository mascotasRepository,
                             VeterinarioRepository veterinarioRepository,
                             ModelMapper modelMapper) {
        this.dietasRepository = dietasRepository;
        this.mascotasRepository = mascotasRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DietasDTO> findAllDietas() {
        return dietasRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public DietasDTO getDietasById(Long idDieta) {
        Dietas dieta = dietasRepository.findById(idDieta)
                .orElseThrow(() -> new CustomException("Dieta no encontrada con id: " + idDieta));
        return convertirADTO(dieta);
    }

    @Override
    public DietasDTO crearDietas(DietasDTO dietasDTO) {
        // Mapear los campos simples del DTO a la entidad
        Dietas dieta = modelMapper.map(dietasDTO, Dietas.class);

        // Buscar las entidades relacionadas para establecer la conexión
        Mascotas mascota = mascotasRepository.findById(dietasDTO.getIdMascota())
                .orElseThrow(() -> new CustomException("Mascota no encontrada con id: " + dietasDTO.getIdMascota()));
        Veterinarios veterinario = veterinarioRepository.findById(dietasDTO.getIdVeterinario())
                .orElseThrow(() -> new CustomException("Veterinario no encontrado con id: " + dietasDTO.getIdVeterinario()));

        // Establecer las relaciones en la entidad
        dieta.setMascota(mascota);
        dieta.setVeterinario(veterinario);

        // Guardar y devolver el DTO convertido
        Dietas dietaGuardada = dietasRepository.save(dieta);
        return convertirADTO(dietaGuardada);
    }

    @Override
    public DietasDTO updateDietas(Long idDieta, DietasDTO dietasDTO) {
        // Buscar la dieta existente
        Dietas dietaExistente = dietasRepository.findById(idDieta)
                .orElseThrow(() -> new CustomException("Dieta no encontrada con id: " + idDieta));

        // Buscar las entidades relacionadas
        Mascotas mascota = mascotasRepository.findById(dietasDTO.getIdMascota())
                .orElseThrow(() -> new CustomException("Mascota no encontrada con id: " + dietasDTO.getIdMascota()));
        Veterinarios veterinario = veterinarioRepository.findById(dietasDTO.getIdVeterinario())
                .orElseThrow(() -> new CustomException("Veterinario no encontrado con id: " + dietasDTO.getIdVeterinario()));

        // Actualizar los campos y relaciones
        dietaExistente.setDescripcion(dietasDTO.getDescripcion());
        dietaExistente.setTipoDieta(dietasDTO.getTipoDieta());
        dietaExistente.setDieta(dietasDTO.getDieta());
        dietaExistente.setMascota(mascota);
        dietaExistente.setVeterinario(veterinario);

        // Guardar y convertir a DTO
        Dietas dietaActualizada = dietasRepository.save(dietaExistente);
        return convertirADTO(dietaActualizada);
    }

    @Override
    public void deleteDietas(Long idDieta) {
        if (!dietasRepository.existsById(idDieta)) {
            throw new CustomException("No se puede eliminar. Dieta no encontrada con id: " + idDieta);
        }
        dietasRepository.deleteById(idDieta);
    }

    /**
     * Nuevo método para obtener las dietas de una mascota específica.
     */
    @Override
    public List<DietasDTO> findDietasByMascotaId(Long idMascota) {
        List<Dietas> dietas = dietasRepository.findByMascota_IdMascota(idMascota);
        return dietas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Método helper para convertir una entidad Dieta a su DTO,
     * enriqueciendo el DTO con los nombres de la mascota y el veterinario.
     */
    private DietasDTO convertirADTO(Dietas dieta) {
        DietasDTO dto = modelMapper.map(dieta, DietasDTO.class);

        // Añadir información de las relaciones para usar en la vista
        if (dieta.getMascota() != null) {
            dto.setNombreMascota(dieta.getMascota().getNombre());
        }

        if (dieta.getVeterinario() != null && dieta.getVeterinario().getUsuario() != null) {
            String nombreCompletoVet = dieta.getVeterinario().getUsuario().getNombre() + " " + dieta.getVeterinario().getUsuario().getApellido();
            dto.setNombreVeterinario(nombreCompletoVet);
        }

        return dto;
    }
}