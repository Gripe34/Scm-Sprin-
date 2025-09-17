package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Usuarios findByIdUsuario(Long idUsuario);

    Optional<Usuarios> findBycorreo(String correo);

}
