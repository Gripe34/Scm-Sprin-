package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import com.miproyecto.demo.entity.ActividadFisica;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.ActividadRepository;
import com.miproyecto.demo.service.ActividadFisicaService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadFServiceImpl implements ActividadFisicaService {

    private final ActividadRepository actividadRepository;
    private final ModelMapper modelMapper;

    public ActividadFServiceImpl(ActividadRepository actividadRepository, ModelMapper modelMapper) {
        this.actividadRepository = actividadRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ActividadFisicaDTO> findAllActividadFisica() {
        return actividadRepository.findAll()
                .stream()
                .map(a -> modelMapper.map(a, ActividadFisicaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ActividadFisicaDTO getActividadById(Long idActividadF) {
        ActividadFisica actividad = actividadRepository.findById(idActividadF)
                .orElseThrow(() -> new CustomException("Actividad no encontrada: " + idActividadF));
        return modelMapper.map(actividad, ActividadFisicaDTO.class);
    }

    @Override
    public ActividadFisicaDTO crearActividadFisica(ActividadFisicaDTO actividadFisicaDTO) {
        if (actividadFisicaDTO.getIdActividadF() != null && actividadRepository.existsById(actividadFisicaDTO.getIdActividadF())) {
            throw new CustomException("El id ya existe: " + actividadFisicaDTO.getIdActividadF());
        }

        ActividadFisica actividad = modelMapper.map(actividadFisicaDTO, ActividadFisica.class);
        actividad = actividadRepository.save(actividad);
        return modelMapper.map(actividad, ActividadFisicaDTO.class);
    }


    @Override
    public ActividadFisicaDTO updateActividadFisica(Long idActividadF, ActividadFisicaDTO actividadFisicaDTO) {
        ActividadFisica actividadExistente = actividadRepository.findById(idActividadF)
                .orElseThrow(() -> new CustomException("Actividad no encontrada: " + idActividadF));

        actividadExistente.setDescripcion(actividadFisicaDTO.getDescripcion());
        actividadExistente.setTipoActividad(actividadFisicaDTO.getTipoActividad());
        actividadExistente.setFoto(actividadFisicaDTO.getFoto());

        actividadExistente = actividadRepository.save(actividadExistente);
        return modelMapper.map(actividadExistente, ActividadFisicaDTO.class);
    }

    @Override
    public void deleteActividadFisica(Long idActividadF) {
        if (!actividadRepository.existsById(idActividadF)) {
            throw new CustomException("Actividad no encontrada: " + idActividadF);
        }
        actividadRepository.deleteById(idActividadF);
    }
}
