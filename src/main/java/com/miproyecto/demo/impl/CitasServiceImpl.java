package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.CitasDTO;
import com.miproyecto.demo.entity.Citas;
import com.miproyecto.demo.entity.Mascotas;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.entity.DiagnosticoDueno;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.CitasRepository;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.repository.DiagnosticoRepository;
import com.miproyecto.demo.service.CitasService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CitasServiceImpl implements CitasService {

    private final CitasRepository citasRepository;
    private final MascotasRepository mascotasRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CitasServiceImpl(
            CitasRepository citasRepository,
            MascotasRepository mascotasRepository,
            VeterinarioRepository veterinarioRepository,
            DiagnosticoRepository diagnosticoRepository,
            ModelMapper modelMapper
    ) {
        this.citasRepository = citasRepository;
        this.mascotasRepository = mascotasRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CitasDTO> findAllCitas() {
        List<Citas> citas = citasRepository.findAll();
        return citas.stream()
                .map(c -> modelMapper.map(c, CitasDTO.class))
                .toList(); // o collect(Collectors.toList()) si usas Java < 16
    }

    @Override
    public CitasDTO getCitasById(Long idCita) {
        return citasRepository.findById(idCita)
                .map(c -> modelMapper.map(c, CitasDTO.class))
                .orElseThrow(() -> new CustomException("Cita no encontrada con id:" + idCita));
    }

    // En tu archivo CitasServiceImpl.java

    @Override
    @Transactional
    public CitasDTO crearCitas(CitasDTO citasDTO) {
        // 1. Buscar las entidades relacionadas...
        Mascotas mascota = mascotasRepository.findById(citasDTO.getIdMascota())
                .orElseThrow(() -> new CustomException("Mascota no encontrada..."));

        Veterinarios veterinario = veterinarioRepository.findById(citasDTO.getIdVeterinario())
                .orElseThrow(() -> new CustomException("Veterinario no encontrado..."));

        DiagnosticoDueno diagnostico = diagnosticoRepository.findById(citasDTO.getIdDiagnostico())
                .orElseThrow(() -> new CustomException("Diagnóstico no encontrado..."));

        // 2. Mapear los datos del DTO a la entidad
        Citas nuevaCita = modelMapper.map(citasDTO, Citas.class);

        // 3. Establecer las relaciones...
        nuevaCita.setMascota(mascota);
        nuevaCita.setVeterinario(veterinario);
        nuevaCita.setDiagnostico(diagnostico);

        // 4. *** LA SOLUCIÓN ESTÁ AQUÍ ***
        //    Asignar un estado por defecto antes de guardar.
        nuevaCita.setEstadoCita("Pendiente");

        // 5. Guardar la nueva cita...
        Citas citaGuardada = citasRepository.save(nuevaCita);

        // 6. Devolver el DTO...
        return modelMapper.map(citaGuardada, CitasDTO.class);
    }

    @Override
    public CitasDTO updateCitas(Long idCita, CitasDTO citasDTO) {
        Citas citaExistente = citasRepository.findById(idCita)
                .orElseThrow(() -> new CustomException("Cita no encontrada con id:" + idCita));

        // Actualizar relaciones
        if (citasDTO.getIdMascota() != null) {
            Mascotas mascota = mascotasRepository.findById(citasDTO.getIdMascota())
                    .orElseThrow(() -> new CustomException("Mascota no encontrada"));
            citaExistente.setMascota(mascota);
        }

        if (citasDTO.getIdVeterinario() != null) {
            Veterinarios veterinario = veterinarioRepository.findById(citasDTO.getIdVeterinario())
                    .orElseThrow(() -> new CustomException("Veterinario no encontrado"));
            citaExistente.setVeterinario(veterinario);
        }

        if (citasDTO.getIdDiagnostico() != null) {
            DiagnosticoDueno diagnostico = diagnosticoRepository.findById(citasDTO.getIdDiagnostico())
                    .orElseThrow(() -> new CustomException("Diagnóstico no encontrado"));
            citaExistente.setDiagnostico(diagnostico);
        }

        // Campos simples
        citaExistente.setFechaCita(citasDTO.getFechaCita());
        citaExistente.setMotivoCita(citasDTO.getMotivoCita());
        citaExistente.setEstadoCita(citasDTO.getEstadoCita());

        citaExistente = citasRepository.save(citaExistente);
        return modelMapper.map(citaExistente, CitasDTO.class);
    }

    @Override
    public void deleteCitas(long idCita) {
        Citas citaExistente = citasRepository.findById(idCita)
                .orElseThrow(() -> new CustomException("Cita no encontrada con id:" + idCita));
        citasRepository.delete(citaExistente);
    }
}
