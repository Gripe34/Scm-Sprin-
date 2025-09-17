package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.entity.Mascotas;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.service.MascotasService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotasServiceImpl implements MascotasService {

    private final MascotasRepository mascotasRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MascotasServiceImpl(MascotasRepository mascotasRepository, ModelMapper modelMapper){
        this.mascotasRepository = mascotasRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<MascotasDTO> findAllMascotas() {
        List<Mascotas> mascotas = mascotasRepository.findAll();
        return mascotas.stream()
                .map(mascota ->  modelMapper.map(mascota, MascotasDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MascotasDTO getMascotasById(Long idMascota) {
        return mascotasRepository.findById(idMascota)
                .map(mascota -> modelMapper.map(mascota, MascotasDTO.class))
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + idMascota));
    }

    @Override
    public MascotasDTO crearMascotas(MascotasDTO mascotasDTO) {
        if (mascotasDTO.getIdMascota() != null && mascotasRepository.existsById(mascotasDTO.getIdMascota())) {
            throw new CustomException("El id ya existe ");
        }
        Mascotas mascotas = modelMapper.map(mascotasDTO, Mascotas.class);
        return modelMapper.map(mascotas, MascotasDTO.class);
    }

    @Override
    public MascotasDTO updateMascotas(Long idMascota, MascotasDTO mascotasDTO) {
        Mascotas mascotasExistente = mascotasRepository.findById(idMascota)
                .orElseThrow(() -> new CustomException("Mascota no encontrada con id: " + idMascota));

        mascotasExistente.setNombre(mascotasDTO.getNombre());
        // Actualiza otros atributos aquí

        mascotasExistente = mascotasRepository.save(mascotasExistente);
        return modelMapper.map(mascotasExistente, MascotasDTO.class);
    }


    @Override
    public void deleteMascotas(Long idMascota) {
        Mascotas mascotasExistente = mascotasRepository.findById(idMascota)
                .orElseThrow(() -> new CustomException("Mascota no encontrada con id : " + idMascota));
        mascotasRepository.delete(mascotasExistente);
    }

}
