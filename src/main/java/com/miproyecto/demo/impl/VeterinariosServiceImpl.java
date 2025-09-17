package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.dto.VeterinariosDTO;
import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.repository.VeterinarioRepository;
import com.miproyecto.demo.service.UsuariosService;
import com.miproyecto.demo.service.VeterinarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class VeterinariosServiceImpl implements VeterinarioService {

    private static final String VETERINARIO_NOT_FOUND = "Veterinario no encontrado con id: ";

    private final VeterinarioRepository veterinarioRepository;
    private final ModelMapper modelMapper;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuariosService usuariosService;

    @Autowired
    public VeterinariosServiceImpl(VeterinarioRepository veterinarioRepository, ModelMapper modelMapper, UsuariosService usuariosService, PasswordEncoder passwordEncoder, RolesRepository rolesRepository){
        this.veterinarioRepository = veterinarioRepository;
        this.modelMapper = modelMapper;
        this.usuariosService = usuariosService;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public List<VeterinariosDTO> findAllVeterinarios() {
        List<Veterinarios> veterinarios = veterinarioRepository.findAll();
        return veterinarios.stream()
                .map(v -> modelMapper.map(v, VeterinariosDTO.class))
                .toList(); // en Java 16+, o usa Collectors.toList() si necesitas modificable
    }

    @Override
    public VeterinariosDTO getVeterionarioById(Long idVeterinarios) {
        return veterinarioRepository.findById(idVeterinarios)
                .map(veterinarios -> modelMapper.map(veterinarios, VeterinariosDTO.class))
                .orElseThrow(() -> new RuntimeException(VETERINARIO_NOT_FOUND + idVeterinarios));
    }

    @Override
    public VeterinariosDTO crearVeterinario(VeterinariosDTO veterinariosDTO) {

        UsuariosDTO usuarioDTO = new UsuariosDTO();
        usuarioDTO.setCorreo(veterinariosDTO.getCorreo());
        usuarioDTO.setContrasena(passwordEncoder.encode(veterinariosDTO.getContrasena()));


        Roles rolVeterinario = rolesRepository.findByRol("veterinario")
                .orElseThrow(() -> new CustomException("El rol 'veterinario' no existe"));
        usuarioDTO.setIdRol(rolVeterinario.getIdRol());
        UsuariosDTO usuarioCreado = usuariosService.crearUsuario(usuarioDTO);

        Veterinarios veterinario = modelMapper.map(veterinariosDTO, Veterinarios.class);
        veterinario.setUsuario(modelMapper.map(usuarioCreado, Usuarios.class));
        Veterinarios veterinarioGuardado = veterinarioRepository.save(veterinario);

        return modelMapper.map(veterinarioGuardado, VeterinariosDTO.class);
    }

    @Override
    public VeterinariosDTO updateVeterinario(Long idVeterinario, VeterinariosDTO veterinariosDTO) {
        Veterinarios veterinarioExistente = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new CustomException(VETERINARIO_NOT_FOUND + idVeterinario));

        veterinarioExistente.setNombre(veterinariosDTO.getNombre());
        veterinarioExistente.setEspecialidad(veterinariosDTO.getEspecialidad());
        veterinarioExistente = veterinarioRepository.save(veterinarioExistente);
        return modelMapper.map(veterinarioExistente, VeterinariosDTO.class);
    }

    @Override
    public void deleteVeterinario(Long idVeterinario) {
        Veterinarios veterinarios = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new CustomException(VETERINARIO_NOT_FOUND + idVeterinario));
        veterinarioRepository.delete(veterinarios);
    }
}
