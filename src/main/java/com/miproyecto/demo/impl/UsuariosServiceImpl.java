package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.MascotasService;
import com.miproyecto.demo.service.UsuariosService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuariosServiceImpl implements UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuariosServiceImpl(UsuariosRepository usuariosRepository, ModelMapper modelMapper, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;

    }
    @Autowired
    private MascotasService mascotasService; // Inyecta el servicio de mascotas

    @Override
    public List<UsuariosDTO> findAllUsuarios() {
        List<Usuarios> usuarios = usuariosRepository.findAll();
        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuariosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UsuariosDTO> obtenerUsuariosConPaginacion(Pageable pageable) {
        // Asume que el rol de cliente es "CLIENTE" en la base de datos
        Page<Usuarios> clientesPage = usuariosRepository.findByRol_Rol("CLIENTE", pageable);
        return clientesPage.map(usuario -> modelMapper.map(usuario, UsuariosDTO.class));
    }

    @Override
    public String restablecerContrasena(Long idUsuario) {
        Usuarios usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuario));

        String nuevaContrasena = UUID.randomUUID().toString().substring(0, 8);
        String contrasenaEncriptada = passwordEncoder.encode(nuevaContrasena);

        usuario.setContrasena(contrasenaEncriptada);
        usuariosRepository.save(usuario);

        // Retorna la contraseña SIN encriptar para mostrarla/enviarla
        return nuevaContrasena;
    }

    @Override
    public UsuariosDTO getUsuariosById(Long idUsuarios) {
        return usuariosRepository.findById(idUsuarios)
                .map(usuario -> modelMapper.map(usuario, UsuariosDTO.class))
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuarios));
    }

    @Override
    public UsuariosDTO crearUsuario(UsuariosDTO usuariosDTO) {
        if (usuariosDTO.getIdUsuario() != null && usuariosRepository.existsById(usuariosDTO.getIdUsuario())) {
            throw new CustomException("El id ya existe");
        }
        // Asegúrate de encriptar la contraseña al crear un nuevo usuario
        usuariosDTO.setContrasena(passwordEncoder.encode(usuariosDTO.getContrasena()));

        Usuarios usuarios = modelMapper.map(usuariosDTO, Usuarios.class);

        // Asignar rol
        if (usuariosDTO.getIdRol() != null) {
            Roles rol = rolesRepository.findById(usuariosDTO.getIdRol())
                    .orElseThrow(() -> new CustomException("Rol no encontrado"));
            usuarios.setRol(rol);
        }

        usuarios = usuariosRepository.save(usuarios);
        return modelMapper.map(usuarios, UsuariosDTO.class);
    }

    @Override
    public UsuariosDTO updateUsuarios(Long idUsuarios, UsuariosDTO usuariosDTO) {
        Usuarios usuariosExistente = usuariosRepository.findById(idUsuarios)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuarios));

        usuariosExistente.setNombre(usuariosDTO.getNombre());
        usuariosExistente.setApellido(usuariosDTO.getApellido());
        usuariosExistente.setCorreo(usuariosDTO.getCorreo());
        usuariosExistente.setTelefono(usuariosDTO.getTelefono());
        usuariosExistente.setDireccion(usuariosDTO.getDireccion());
        usuariosExistente.setFoto(usuariosDTO.getFoto());

        // Solo actualiza la contraseña si se provee una nueva (no nula y no vacía)
        if (usuariosDTO.getContrasena() != null && !usuariosDTO.getContrasena().trim().isEmpty()) {
            usuariosExistente.setContrasena(passwordEncoder.encode(usuariosDTO.getContrasena()));
        }

        if (usuariosDTO.getIdRol() != null) {
            Roles rol = rolesRepository.findById(usuariosDTO.getIdRol())
                    .orElseThrow(() -> new CustomException("Rol no encontrado con id: " + usuariosDTO.getIdRol()));
            usuariosExistente.setRol(rol);
        }

        Usuarios actualizado = usuariosRepository.save(usuariosExistente);
        return modelMapper.map(actualizado, UsuariosDTO.class);
    }

    @Override
    public void deleteUsuarios(Long idUsuarios) {
        Usuarios usuarioExistente = usuariosRepository.findById(idUsuarios)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id:" + idUsuarios));

        // Regla de negocio para no eliminar administradores
        if (usuarioExistente.getRol() != null && "administrador".equalsIgnoreCase(usuarioExistente.getRol().getRol())) {
            throw new CustomException("No se puede eliminar un usuario administrador.");
        }

        usuariosRepository.delete(usuarioExistente);
    }

    @Override
    public List<UsuariosDTO> findDuenosByVeterinarioId(Long idVeterinario) {
        return List.of();
    }

    @Override
    public List<UsuariosDTO> findDuenosByVeterinarioId(Long idVeterinario, String nombre, String apellido, String correo) {
        List<Usuarios> duenos;
        if ((nombre != null && !nombre.trim().isEmpty()) ||
                (apellido != null && !apellido.trim().isEmpty()) ||
                (correo != null && !correo.trim().isEmpty())) {
            duenos = usuariosRepository.findDuenosByVeterinarioConFiltros(idVeterinario, nombre, apellido, correo);
        } else {
            duenos = usuariosRepository.findDuenosByVeterinarioId(idVeterinario);
        }

        // Convierte la lista de entidades a DTOs
        return duenos.stream()
                .map(dueno -> {
                    // 1. Mapea los datos básicos del dueño
                    UsuariosDTO dto = modelMapper.map(dueno, UsuariosDTO.class);

                    // 2. Busca las mascotas de este dueño usando el MascotasService
                    List<MascotasDTO> mascotasDelDueno = mascotasService.obtenerMascotasPorDuenoId(dueno.getIdUsuario());

                    // 3. Asigna la lista de mascotas al DTO del dueño
                    dto.setMascotas(mascotasDelDueno);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void bloquearUsuario(Long idUsuario) {
        Usuarios usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuario));

        // Asume que la entidad Usuarios tiene el campo "boolean habilitado"
        usuario.setHabilitado(!usuario.isHabilitado());
        usuariosRepository.save(usuario);
    }
    @Override
    public List<UsuariosDTO> findAllByIds(List<Long> ids) {
        // Llama al método que ya existe en JpaRepository
        List<Usuarios> usuarios = usuariosRepository.findAllById(ids);

        // Convierte la lista de entidades a DTOs
        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuariosDTO.class))
                .collect(Collectors.toList());
    }
}