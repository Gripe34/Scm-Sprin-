package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.DiagnosticoDuenoDTO;
import com.miproyecto.demo.entity.DiagnosticoDueno;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.DiagnosticoRepository;
import com.miproyecto.demo.service.DiagnosticoDuenoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoDuenoService {

    private  final DiagnosticoRepository diagnosticoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DiagnosticoServiceImpl(DiagnosticoRepository diagnosticoRepository, ModelMapper modelMapper) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.modelMapper = modelMapper;
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
    public DiagnosticoDuenoDTO crearDiagnostico(DiagnosticoDuenoDTO diagnosticoDuenoDTO) {
        if (diagnosticoDuenoDTO.getIdDiagnostico() != null && diagnosticoRepository.existsById(diagnosticoDuenoDTO.getIdDiagnostico())) {
            throw new CustomException("El id ya existe");
        }
        DiagnosticoDueno diagnosticoDueno = modelMapper.map(diagnosticoDuenoDTO, DiagnosticoDueno.class);
        diagnosticoDueno = diagnosticoRepository.save(diagnosticoDueno);
        return modelMapper.map(diagnosticoDueno, DiagnosticoDuenoDTO.class);
    }

    @Override
    public DiagnosticoDuenoDTO updateDiagnostico(Long idDiagnostico, DiagnosticoDuenoDTO diagnosticoDuenoDTO) {
        DiagnosticoDueno diagnosticoDuenoExistente = diagnosticoRepository.findById(idDiagnostico)
                .orElseThrow(() -> new CustomException("Diagnostico no encontrado con id: " + idDiagnostico));

        diagnosticoDuenoExistente.setIdDiagnostico(diagnosticoDuenoDTO.getIdDiagnostico());
        diagnosticoDuenoExistente.setIdMedico(diagnosticoDuenoDTO.getIdMedico());
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
}
