package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.UsuariosService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsuariosServiceImpl implements UsuariosService{

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;

    @Autowired

    public UsuariosServiceImpl(UsuariosRepository usuariosRepository,  ModelMapper modelMapper, RolesRepository rolesRepository){
        this.usuariosRepository= usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UsuariosDTO> findAllUsuarios() {
        List<Usuarios> usuarios = usuariosRepository.findAll();
        return usuarios.stream()
                .map(usuarios1 -> modelMapper.map(usuarios1, UsuariosDTO.class))
                .toList();
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
        Usuarios usuariosExistente = usuariosRepository.findById(idUsuarios)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id: " + idUsuarios));

        usuariosExistente.setNombre(usuariosDTO.getNombre());
        usuariosExistente.setApellido(usuariosDTO.getApellido());
        usuariosExistente.setCorreo(usuariosDTO.getCorreo());
        usuariosExistente.setContrasena(usuariosDTO.getContrasena());
        usuariosExistente.setTelefono(usuariosDTO.getTelefono());
        usuariosExistente.setDireccion(usuariosDTO.getDireccion());
        usuariosExistente.setFoto(usuariosDTO.getFoto());

        // Aquí asignas el rol usando el repositorio de Roles
        if (usuariosDTO.getIdRol() != null) {
            Roles rol = rolesRepository.findById(usuariosDTO.getIdRol())
                    .orElseThrow(() -> new CustomException("Rol no encontrado con id: " + usuariosDTO.getIdRol()));
            usuariosExistente.setRol(rol);
        }

        // Guardar cambios
        Usuarios actualizado = usuariosRepository.save(usuariosExistente);

        // Mapear de vuelta a DTO
        UsuariosDTO resp = modelMapper.map(actualizado, UsuariosDTO.class);
        if (actualizado.getRol() != null) {
            resp.setIdRol(actualizado.getRol().getIdRol());
        }
        return resp;
    }



    @Override
    public void deleteUsuarios(Long idUsuarios) {
        Usuarios usuarioExistente = usuariosRepository.findById(idUsuarios)
                .orElseThrow(() -> new CustomException("Usuario no encontrado con id:" + idUsuarios));
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
    }}
