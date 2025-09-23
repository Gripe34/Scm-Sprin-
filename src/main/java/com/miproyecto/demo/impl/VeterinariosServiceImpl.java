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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class VeterinariosServiceImpl implements VeterinarioService {

    private static final String VETERINARIO_NOT_FOUND = "Veterinario no encontrado con id: ";

    private final VeterinarioRepository veterinarioRepository;
    private final ModelMapper modelMapper;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuariosService usuariosService;
    private final UsuariosRepository usuariosRepository;


    @Autowired
    public VeterinariosServiceImpl(VeterinarioRepository veterinarioRepository, ModelMapper modelMapper, UsuariosService usuariosService, PasswordEncoder passwordEncoder, RolesRepository rolesRepository,UsuariosRepository usuariosRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.modelMapper = modelMapper;
        this.usuariosService = usuariosService;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.usuariosRepository = usuariosRepository;
    }


    @Override
    public Page<VeterinariosDTO> obtenerVeterinariosConPaginacion(Pageable pageable) {
        // 1. Obtiene la página de entidades de la base de datos.
        Page<Veterinarios> veterinariosPage = veterinarioRepository.findAll(pageable);

        // 2. Convierte cada entidad a un DTO de forma manual.
        return veterinariosPage.map(veterinario -> {
            VeterinariosDTO dto = new VeterinariosDTO();

            // 3. Asigna los campos del veterinario directamente al DTO.
            dto.setIdVeterinario(veterinario.getIdVeterinario());
            dto.setEspecialidad(veterinario.getEspecialidad());

            // 4. Asigna los campos del usuario al DTO, manejando el caso nulo.
            if (veterinario.getUsuario() != null) {
                dto.setNombre(veterinario.getUsuario().getNombre());
                dto.setApellido(veterinario.getUsuario().getApellido());
                dto.setTelefono(veterinario.getUsuario().getTelefono());
                dto.setCorreo(veterinario.getUsuario().getCorreo());
                dto.setDireccion(veterinario.getUsuario().getDireccion());

                // ¡Aquí está la línea que faltaba!
                dto.setHabilitado(veterinario.getUsuario().isHabilitado());
            }

            return dto;
        });
    }

    @Override
    public List<VeterinariosDTO> findAllVeterinarios() {
        return veterinarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private VeterinariosDTO convertirADTO(Veterinarios veterinario) {
        VeterinariosDTO dto = new VeterinariosDTO();
        dto.setIdVeterinario(veterinario.getIdVeterinario());
        dto.setEspecialidad(veterinario.getEspecialidad());

        if (veterinario.getUsuario() != null) {
            Usuarios usuario = veterinario.getUsuario();
            dto.setNombre(usuario.getNombre());

            dto.setApellido(usuario.getApellido());
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setCorreo(usuario.getCorreo());
            dto.setTelefono(usuario.getTelefono());
            dto.setDireccion(usuario.getDireccion());
        }
        return dto;
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
        usuario.setHabilitado(true); // Se agrega para que por defecto se habilite


        Roles rolVeterinario = rolesRepository.findByRol("veterinario")
                .orElseThrow(() -> new CustomException("El rol 'veterinario' no existe"));
        usuario.setRol(rolVeterinario);
        Usuarios usuarioGuardado = usuariosRepository.save(usuario);


        Veterinarios veterinario = new Veterinarios();
        veterinario.setEspecialidad(veterinariosDTO.getEspecialidad());

        veterinario.setUsuario(usuarioGuardado);
        Veterinarios veterinarioGuardado = veterinarioRepository.save(veterinario);

        return modelMapper.map(veterinarioGuardado, VeterinariosDTO.class);
    }

    @Override
    public VeterinariosDTO updateVeterinario(Long idVeterinario, VeterinariosDTO veterinariosDTO) {
        Veterinarios veterinarioExistente = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new CustomException(VETERINARIO_NOT_FOUND + idVeterinario));


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
