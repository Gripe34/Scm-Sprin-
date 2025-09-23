package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.ClienteRepository;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.UsuariosService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UsuariosServiceImpl implements UsuariosService{

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;

    @Autowired

    public UsuariosServiceImpl(UsuariosRepository usuariosRepository,  ModelMapper modelMapper, RolesRepository rolesRepository, PasswordEncoder passwordEncoder, ClienteRepository clienteRepository) {
        this.usuariosRepository= usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<UsuariosDTO> findAllUsuarios() {
        List<Usuarios> usuarios = usuariosRepository.findAll();
        return usuarios.stream()
                .map(usuarios1 -> modelMapper.map(usuarios1, UsuariosDTO.class))
                .toList();
    }

    @Override
    public Page<UsuariosDTO> obtenerUsuariosConPaginacion(Pageable pageable) {
        Page<Usuarios> clientesPage = usuariosRepository.findByRol_Rol("CLIENTE", pageable);
        return clientesPage.map(usuario -> modelMapper.map(usuario, UsuariosDTO.class));
    }

    @Override
    public String restablecerContrasena(Long idUsuario) {
        Usuarios usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuario));

        // 1. Genera una contraseña aleatoria
        String nuevaContrasena = UUID.randomUUID().toString().substring(0, 8);

        // 2. Encripta la nueva contraseña
        String contrasenaEncriptada = passwordEncoder.encode(nuevaContrasena);

        // 3. Asigna y guarda la contraseña encriptada
        usuario.setContrasena(contrasenaEncriptada);
        usuariosRepository.save(usuario);

        // 4. Retorna la contraseña sin encriptar para mostrarla en la vista
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
        Usuarios usuarios = modelMapper.map(usuariosDTO, Usuarios.class);
        usuarios = usuariosRepository.save(usuarios);
        return modelMapper.map(usuarios, UsuariosDTO.class);
    }


    @Override
    public UsuariosDTO updateUsuarios(Long idUsuarios, UsuariosDTO usuariosDTO) {
        // 1. Encuentra el usuario existente
        Usuarios usuariosExistente = usuariosRepository.findById(idUsuarios)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuarios));

        // 2. Actualiza los campos que no son la contraseña
        usuariosExistente.setNombre(usuariosDTO.getNombre());
        usuariosExistente.setApellido(usuariosDTO.getApellido());
        usuariosExistente.setCorreo(usuariosDTO.getCorreo());
        usuariosExistente.setTelefono(usuariosDTO.getTelefono());
        usuariosExistente.setDireccion(usuariosDTO.getDireccion());
        usuariosExistente.setFoto(usuariosDTO.getFoto());

        // Solo actualiza la contraseña si no es nula y no está vacía.
        if (usuariosDTO.getContrasena() != null && !usuariosDTO.getContrasena().isEmpty()) {
            usuariosExistente.setContrasena(usuariosDTO.getContrasena());
        }

        // 4. Lógica del Rol (como ya la tienes)
        if (usuariosDTO.getIdRol() != null) {
            Roles rol = rolesRepository.findById(usuariosDTO.getIdRol())
                    .orElseThrow(() -> new CustomException("Rol no encontrado con id: " + usuariosDTO.getIdRol()));
            usuariosExistente.setRol(rol);
        }

        // 5. Guarda y mapea los cambios
        Usuarios actualizado = usuariosRepository.save(usuariosExistente);
        return modelMapper.map(actualizado, UsuariosDTO.class);
    }



    @Override
    @Transactional
    public void deleteUsuarios(Long idUsuarios) {
        Usuarios usuarioExistente = usuariosRepository.findById(idUsuarios)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id:" + idUsuarios));

        if (usuarioExistente.getRol() != null && usuarioExistente.getRol().getIdRol() == 1L) {
            throw new CustomException("No se puede eliminar un usuario administrador.");
        }

        // Esta es la línea corregida: Usa el ClienteRepository para eliminar el registro hijo primero.
        clienteRepository.deleteByUsuario_IdUsuario(idUsuarios);

        // Luego, elimina el registro padre.
        usuariosRepository.delete(usuarioExistente);
    }

    @Override
    public List<UsuariosDTO> findDuenosByVeterinarioId(Long idVeterinario) {
        // Llama al método del repositorio
        List<Usuarios> duenos = usuariosRepository.findDuenosByVeterinarioId(idVeterinario);

        // Mapea los resultados a DTOs
        return duenos.stream()
                .map(dueno -> modelMapper.map(dueno, UsuariosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void bloquearUsuario(Long idUsuario) {
        Usuarios usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuario));

        // Invierte el estado actual del usuario
        usuario.setHabilitado(!usuario.isHabilitado());
        usuariosRepository.save(usuario);
    }


}
