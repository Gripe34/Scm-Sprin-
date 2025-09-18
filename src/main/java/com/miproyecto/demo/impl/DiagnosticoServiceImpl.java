package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.entity.DiagnosticoDueno;
import com.miproyecto.demo.entity.Mascotas;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.DiagnosticoRepository;
import com.miproyecto.demo.service.DiagnosticoDuenoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoDuenoService {

    private  final DiagnosticoRepository diagnosticoRepository;
    private final ModelMapper modelMapper;
    private final com.miproyecto.demo.repository.MascotasRepository mascotasRepository;
    private final com.miproyecto.demo.repository.VeterinarioRepository veterinariosRepository;

    @Autowired
    public DiagnosticoServiceImpl(DiagnosticoRepository diagnosticoRepository, ModelMapper modelMapper, com.miproyecto.demo.repository.MascotasRepository mascotasRepository, com.miproyecto.demo.repository.VeterinarioRepository veterinariosRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.modelMapper = modelMapper;
        this.mascotasRepository = mascotasRepository;
        this.veterinariosRepository = veterinariosRepository;
    }



    @Override
    public List<DiagnosticoDuenoDTO> findAlldiagnosticoDueno() {
        List<DiagnosticoDueno> diagnostico = diagnosticoRepository.findAll();
        return diagnostico.stream()
                .map(user -> modelMapper.map(user, DiagnosticoDuenoDTO.class))
                .toList();
    }


    @Override
    public DiagnosticoDuenoDTO getDiagnosticoDuenoById(Long idDiagnostico) {
        return diagnosticoRepository.findById(idDiagnostico)
                .map(act -> modelMapper.map(act, DiagnosticoDuenoDTO.class))
                .orElseThrow(() -> new CustomException("Diagnostico no encontrado con id :" + idDiagnostico));
    }



    @Override
    @Transactional // Asegura que toda la operación sea una transacción atómica
    public DiagnosticoDuenoDTO crearDiagnostico(DiagnosticoDuenoDTO diagnosticoDuenoDTO) {
        // 1. Buscar la entidad Mascota usando el ID que viene del DTO
        Mascotas mascota = mascotasRepository.findById(diagnosticoDuenoDTO.getIdMascota())
                .orElseThrow(() -> new CustomException("Mascota no encontrada con id: " + diagnosticoDuenoDTO.getIdMascota()));

        // 2. Buscar la entidad Veterinario usando el ID que viene del DTO
        Veterinarios veterinario = veterinariosRepository.findById(diagnosticoDuenoDTO.getIdVeterinarios())
                .orElseThrow(() -> new CustomException("Veterinario no encontrado con id: " + diagnosticoDuenoDTO.getIdVeterinarios()));

        // 3. Mapear los datos simples del DTO (fecha, observaciones) a la entidad
        DiagnosticoDueno nuevoDiagnostico = modelMapper.map(diagnosticoDuenoDTO, DiagnosticoDueno.class);

        // 4. Establecer las relaciones en la entidad con los objetos que encontramos
        nuevoDiagnostico.setMascota(mascota);
        nuevoDiagnostico.setVeterinario(veterinario);

        // 5. Guardar la entidad completa en la base de datos
        DiagnosticoDueno diagnosticoGuardado = diagnosticoRepository.save(nuevoDiagnostico);

        // 6. Devolver el DTO del objeto recién guardado
        return modelMapper.map(diagnosticoGuardado, DiagnosticoDuenoDTO.class);
    }

    @Override
    public DiagnosticoDuenoDTO updateDiagnostico(Long idDiagnostico, DiagnosticoDuenoDTO diagnosticoDuenoDTO) {
        DiagnosticoDueno diagnosticoDuenoExistente = diagnosticoRepository.findById(idDiagnostico)
                .orElseThrow(() -> new CustomException("Diagnostico no encontrado con id: " + idDiagnostico));

        diagnosticoDuenoExistente.setIdDiagnostico(diagnosticoDuenoDTO.getIdDiagnostico());

        diagnosticoDuenoExistente.setFechaDiagnostico(diagnosticoDuenoDTO.getFechaDiagnostico());
        diagnosticoDuenoExistente.setObservaciones(diagnosticoDuenoDTO.getObservaciones());

        diagnosticoDuenoExistente = diagnosticoRepository.save(diagnosticoDuenoExistente);
        return modelMapper.map(diagnosticoDuenoExistente, DiagnosticoDuenoDTO.class);
    }

    @Override
    public void deleteDiagnostico(long idDiagnostico) {
            DiagnosticoDueno diagnosticoDuenoExistente = diagnosticoRepository.findById(idDiagnostico)
                    .orElseThrow(() -> new CustomException("Diagnostico no encontrado con id: " + idDiagnostico));
            diagnosticoRepository.delete(diagnosticoDuenoExistente);
    }

    @Override
    public List<DiagnosticoDuenoDTO> findDiagnosticosByVeterinario(Veterinarios veterinario) {
        List<DiagnosticoDueno> diagnosticos = diagnosticoRepository.findByVeterinario(veterinario);
        return diagnosticos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Este método de conversión es la clave
    private DiagnosticoDuenoDTO convertirADTO(DiagnosticoDueno diagnostico) {
        DiagnosticoDuenoDTO dto = new DiagnosticoDuenoDTO();
        dto.setIdDiagnostico(diagnostico.getIdDiagnosticoDueno());
        dto.setFechaDiagnostico(diagnostico.getFechaDiagnostico());
        dto.setObservaciones(diagnostico.getObservaciones());

        if (diagnostico.getMascota() != null) {
            dto.setIdMascota(diagnostico.getMascota().getIdMascota());
            dto.setNombreMascota(diagnostico.getMascota().getNombre());

            if (diagnostico.getMascota().getUsuario() != null) {
                Usuarios dueno = diagnostico.getMascota().getUsuario();
                dto.setNombreDueno(dueno.getNombre() + " " + dueno.getApellido());
            }
        }

        if (diagnostico.getVeterinario() != null) {
            // El DTO pide idVeterinarios (plural), lo llenamos con el ID del veterinario.
            dto.setIdVeterinarios(diagnostico.getVeterinario().getIdVeterinario());
        }

        return dto;
    }
}
