package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.DietasDTO;
import com.miproyecto.demo.entity.Dietas;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.DietasRepository;
import com.miproyecto.demo.service.DietasService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miproyecto.demo.entity.Veterinarios;


import java.util.List;

@Service
public class DietasServiceImpl implements DietasService {

    private static final String DIETA_NO_ENCONTRADA = "Dieta no encontrada con id: ";

    private final DietasRepository dietasRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DietasServiceImpl(DietasRepository dietasRepository, ModelMapper modelMapper) {
        this.dietasRepository = dietasRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DietasDTO> findAllDietas() {
        List<Dietas> dietas = dietasRepository.findAll();
        return dietas.stream()
                .map(dieta -> modelMapper.map(dieta, DietasDTO.class))
                .toList(); // o .collect(Collectors.toList()) si usas Java < 16
    }

    @Override
    public DietasDTO getDietasById(Long idDieta) {
        return dietasRepository.findById(idDieta)
                .map(dieta -> modelMapper.map(dieta, DietasDTO.class))
                .orElseThrow(() -> new CustomException(DIETA_NO_ENCONTRADA + idDieta));
    }

    @Override
    public DietasDTO crearDietas(DietasDTO dietasDTO) {
        if (dietasDTO.getIdDieta() != null && dietasRepository.existsById(dietasDTO.getIdDieta())) {
            throw new CustomException("El id ya existe: " + dietasDTO.getIdDieta());
        }
        Dietas dieta = modelMapper.map(dietasDTO, Dietas.class);
        dieta = dietasRepository.save(dieta);
        return modelMapper.map(dieta, DietasDTO.class);
    }

    @Override
    public DietasDTO updateDietas(Long idDieta, DietasDTO dietasDTO) {
        Dietas dieta = dietasRepository.findById(idDieta)
                .orElseThrow(() -> new CustomException(DIETA_NO_ENCONTRADA + idDieta));

        // Como Veterinarios es una relación ManyToOne, necesitas crear el objeto
        Veterinarios veterinario = new Veterinarios();
        veterinario.setIdVeterinario(dietasDTO.getIdVeterinario()); // aquí usas el id del DTO

        // Ahora setear los valores
        dieta.setVeterinario(veterinario);
        dieta.setDescripcion(dietasDTO.getDescripcion());
        dieta.setTipoDieta(dietasDTO.getTipoDieta());
        dieta.setDieta(dietasDTO.getDieta());

        dieta = dietasRepository.save(dieta);
        return modelMapper.map(dieta, DietasDTO.class);
    }


    @Override
    public void deleteDietas(Long idDieta) {
        Dietas dietaExistente = dietasRepository.findById(idDieta)
                .orElseThrow(() -> new CustomException(DIETA_NO_ENCONTRADA + idDieta));
        dietasRepository.delete(dietaExistente);
    }
}
