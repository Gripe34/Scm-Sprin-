package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.RolesDTO;
import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.service.RolesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RolesServiceImpl implements RolesService {

    private final RolesRepository rolesReposiory;
    private final ModelMapper modelMapper;

    @Autowired
    public RolesServiceImpl(RolesRepository rolesReposiory, ModelMapper modelMapper){
        this.rolesReposiory = rolesReposiory;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RolesDTO> findAllRoles() {
        List<Roles> roles = rolesReposiory.findAll();
        return roles.stream()
                .map(rol -> modelMapper.map(rol, RolesDTO.class))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public RolesDTO getRolesById(Long idRol) {
        return rolesReposiory.findById(idRol)
                .map(rol -> modelMapper.map(rol, RolesDTO.class))
                .orElseThrow(() -> new CustomException("Rol no encontrado por ir : " + idRol));
    }

    @Override
    public RolesDTO crearRoles(RolesDTO rolesDTO) {
        if (rolesDTO.getIdRol() != null && rolesReposiory.existsById(rolesDTO.getIdRol())) {
            throw new CustomException("El id ya existe : " + rolesDTO.getIdRol());
        }
        Roles roles = modelMapper.map(rolesDTO, Roles.class);
        roles = rolesReposiory.save(roles);
        return modelMapper.map(roles, RolesDTO.class);
    }

    @Override
    public RolesDTO updateRoles(Long idRol, RolesDTO rolesDTO) {
        Roles rolesExistente = rolesReposiory.findById(idRol)
                .orElseThrow(() -> new CustomException("Rol no encontrado con id: " + idRol));
        rolesExistente.setRol(rolesDTO.getRol());
        rolesExistente.setRol(rolesDTO.getRol());


        rolesExistente = rolesReposiory.save(rolesExistente);
        return modelMapper.map(rolesExistente, RolesDTO.class);
    }

    @Override
    public void deleteRoles(Long idRol) {
        Roles rolesExistente = rolesReposiory.findById(idRol)
                .orElseThrow(() -> new CustomException("Rol no encontrado con id: " + idRol));
        rolesReposiory.delete(rolesExistente);
    }
}
