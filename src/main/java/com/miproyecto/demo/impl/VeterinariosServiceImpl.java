package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.dto.VeterinariosDTO;
import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
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
    private UsuariosRepository usuariosRepository;

    @Autowired
    public VeterinariosServiceImpl(VeterinarioRepository veterinarioRepository, ModelMapper modelMapper, UsuariosService usuariosService, PasswordEncoder passwordEncoder, RolesRepository rolesRepository,UsuariosRepository usuariosRepository){
        this.veterinarioRepository = veterinarioRepository;
        this.modelMapper = modelMapper;
        this.usuariosService = usuariosService;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.usuariosRepository = usuariosRepository;
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

        // 1. Crear y guardar el objeto Usuario
        Usuarios usuario = new Usuarios();
        usuario.setCorreo(veterinariosDTO.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(veterinariosDTO.getContrasena()));
        usuario.setNombre(veterinariosDTO.getNombre());
        usuario.setApellido(veterinariosDTO.getApellido());
        usuario.setTelefono(veterinariosDTO.getTelefono());
        usuario.setDireccion(veterinariosDTO.getDireccion());

        // Asignar el rol 'veterinario'
        Roles rolVeterinario = rolesRepository.findByRol("veterinario")
                .orElseThrow(() -> new CustomException("El rol 'veterinario' no existe"));
        usuario.setRol(rolVeterinario);

        // Guardar el usuario primero en su repositorio
        Usuarios usuarioGuardado = usuariosRepository.save(usuario);

        // 2. Crear y guardar el objeto Veterinario
        // Solo se setean los campos que están en la entidad Veterinarios
        Veterinarios veterinario = new Veterinarios();
        veterinario.setEspecialidad(veterinariosDTO.getEspecialidad());

        // Enlazar el usuario recién guardado
        veterinario.setUsuario(usuarioGuardado);

        // Guardar la entidad Veterinario
        Veterinarios veterinarioGuardado = veterinarioRepository.save(veterinario);

        // 3. Devolver el DTO
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
